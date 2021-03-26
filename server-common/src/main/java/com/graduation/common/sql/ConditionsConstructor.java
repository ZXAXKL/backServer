package com.graduation.common.sql;

import tk.mybatis.mapper.entity.Example;

import java.util.Map;

public class ConditionsConstructor {

    //用于添加相等关系
    protected void equal(Example.Criteria criteria, String key, Map<String, Object> conditions) {
        if (conditions.containsKey(key)) {
            criteria.andEqualTo(key, conditions.get(key));
        }
    }

    //用于添加相等关系
    protected void equal(Example.Criteria criteria, String key, Object value) {
        if (value != null) {
            criteria.andEqualTo(key, value);
        }
    }

    //添加不等关系
    protected void notEqual(Example.Criteria criteria, String key, Map<String, Object> conditions) {
        if (conditions.containsKey(key)) {
            criteria.andNotEqualTo(key, conditions.get(key));
        }
    }

    //添加不等关系
    protected void notEqual(Example.Criteria criteria, String key, Object value) {
        if (value != null) {
            criteria.andNotEqualTo(key, value);
        }
    }

    //前缀匹配
    protected void like(Example.Criteria criteria, String key, Map<String, Object> conditions) {
        if (conditions.containsKey(key)) {
            criteria.andLike(key, conditions.get(key).toString() + "%");
        }
    }

    //前缀匹配
    protected void like(Example.Criteria criteria, String key, Object value) {
        if (value != null) {
            criteria.andLike(key, value.toString() + "%");
        }
    }

    //添加大于等于关系
    protected void greaterOrEqual(Example.Criteria criteria, String key, Map<String, Object> conditions) {
        if (conditions.containsKey(key)) {
            criteria.andGreaterThanOrEqualTo(key, conditions.get(key));
        }
    }

    //添加大于等于关系
    protected void greaterOrEqual(Example.Criteria criteria, String key, Object value) {
        if (value != null) {
            criteria.andGreaterThanOrEqualTo(key, value);
        }
    }

    //添加大于等于关系
    protected void greaterOrEqual(Example.Criteria criteria, String key, String condition, Map<String, Object> conditions) {
        if (conditions.containsKey(condition)) {
            criteria.andGreaterThanOrEqualTo(key, conditions.get(condition));
        }
    }

    //添加大于等于关系
    protected void greaterOrEqual(Example.Criteria criteria, String key, String condition, Object value) {
        if (value != null) {
            criteria.andGreaterThanOrEqualTo(key, value);
        }
    }

    //添加小于等于关系
    protected void lessOrEqual(Example.Criteria criteria, String key, Map<String, Object> conditions) {
        if (conditions.containsKey(key)) {
            criteria.andLessThanOrEqualTo(key, conditions.get(key));
        }
    }

    //添加小于等于关系
    protected void lessOrEqual(Example.Criteria criteria, String key, Object value) {
        if (value != null) {
            criteria.andLessThanOrEqualTo(key, value);
        }
    }

    //添加小于等于关系
    protected void lessOrEqual(Example.Criteria criteria, String key, String condition, Map<String, Object> conditions) {
        if (conditions.containsKey(condition)) {
            criteria.andLessThanOrEqualTo(key, conditions.get(condition));
        }
    }

    // 添加小于等于关系
    protected void lessOrEqual(Example.Criteria criteria, String key, String condition, Object value) {
        if (value != null) {
            criteria.andLessThanOrEqualTo(key, value);
        }
    }

}
