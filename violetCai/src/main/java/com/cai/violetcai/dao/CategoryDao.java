package com.cai.violetcai.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDao {
    //根据用户id及类别名获取类别id
    public int getCategoryId(int userId, String cateName);
}
