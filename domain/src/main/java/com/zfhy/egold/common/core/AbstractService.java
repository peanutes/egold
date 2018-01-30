package com.zfhy.egold.common.core;


import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;


public abstract class AbstractService<T> implements Service<T> {

    @Autowired
    protected Mapper<T> mapper;

    private Class<T> modelClass;    

    public AbstractService() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        modelClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    public void save(T model) {
        mapper.insertSelective(model);
    }

    public void save(List<T> models) {
        mapper.insertList(models);
    }

    public void deleteById(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }

    public void deleteByIds(String ids) {
        mapper.deleteByIds(ids);
    }

    public void update(T model) {
        mapper.updateByPrimaryKeySelective(model);
    }

    @Transactional(readOnly = true)
    public T findById(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Transactional(readOnly = true)
    @Override
    public T findBy(String fieldName, Object value) throws TooManyResultsException {
        try {
            T model = modelClass.newInstance();
            Field field = modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(model, value);
            return mapper.selectOne(model);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<T> findByIds(String ids) {
        return mapper.selectByIds(ids);
    }

    @Transactional(readOnly = true)
    public List<T> findByCondition(Condition condition) {
        return mapper.selectByCondition(condition);
    }

    @Transactional(readOnly = true)
    public List<T> findAll() {
        return mapper.selectAll();
    }

    
    @Transactional(readOnly = true)
    public T fineOneByCondition(Condition condition) {
         List<T>  list =  mapper.selectByCondition(condition);
         T detail = null;

         if(!list.isEmpty()){
             detail = list.get(0);
         }

         return detail;
    }



}
