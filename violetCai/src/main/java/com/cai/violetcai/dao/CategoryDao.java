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
    //获取用户全部类别（降序）
    public List<Category> getAllCategoryByUserId2(@Param("userId") int id);
    //类别id查类别名
    public String getCategoryNameById(int categoryId);
    //获取类别总数
    public int getCategoryCount(@Param("userId") int id);
    //添加类别
    public int addCategory(Category category);
    //删除分类
    public int deleteMultipleCate(List<Integer> id);
    //移动分类
    public int moveCate(List<Integer> id, int userId);
    //更改类名
    public int updateCateName(String cateName, int cateId);
}
