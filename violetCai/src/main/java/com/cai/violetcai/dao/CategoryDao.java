package com.cai.violetcai.dao;

import com.cai.violetcai.bean.Category;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDao {
    //根据用户id及类别名获取类别id
    public int getCategoryId(int userId, String cateName);
    //获取用户全部类别
    public List<Category> getAllCategoryByUserId(@Param("userId") int id);
    //获取类别总数
    public int getCategoryCount(@Param("userId") int id);
    //添加类别
    public int addCategory(Category category);
}
