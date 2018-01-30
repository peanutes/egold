package com.zfhy.egold.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by LAI on 2017/9/27.
 */
public class SqlUtil {

    public static String RIGHE_LIKE = "_RLIKE";
    public static String FULL_LIKE = "_FLIKE";
    public static String NOT = "_NOT";



    /**
     * 封装sql条件参数
     *
     * @param params
     * @param condition
     */
    public static void genSqlCondition(@RequestParam Map<String, String> params, Condition condition) {
        Example.Criteria criteria = condition.createCriteria();
        List<Map.Entry<String, String>> parameters = params.entrySet().stream()
                .filter(entry -> StringUtils.isNotBlank(entry.getValue()))
                .filter(entry -> !"_".equals(entry.getKey()))
                .filter(entry -> !"page".equalsIgnoreCase(entry.getKey()) && !"size".equalsIgnoreCase(entry.getKey()))
                .collect(Collectors.toList());

        for (Map.Entry<String, String> entry : parameters) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key.endsWith(RIGHE_LIKE)) {
                criteria.andLike(key.replace(RIGHE_LIKE, ""), String.join("", value, "%"));
            } else if (key.endsWith(FULL_LIKE)) {
                criteria.andLike(key.replace(FULL_LIKE, ""), String.join("", "%", value, "%"));
            } else if (key.endsWith(NOT)) {
                criteria.andNotEqualTo(key.replace(NOT, ""), value);
            } else {
                criteria.andEqualTo(key, value);
            }
        }
    }



}
