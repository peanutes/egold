package com.zfhy.egold.common.core;

import org.apache.ibatis.exceptions.TooManyResultsException;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;


public interface Service<T> {
    void save(T model);
    void save(List<T> models);
    void deleteById(Integer id);
    void deleteByIds(String ids);
    void update(T model);
    T findById(Integer id);
    T findBy(String fieldName, Object value) throws TooManyResultsException; 
    List<T> findByIds(String ids);
    List<T> findByCondition(Condition condition);
    List<T> findAll();
    T fineOneByCondition(Condition condition);


}
