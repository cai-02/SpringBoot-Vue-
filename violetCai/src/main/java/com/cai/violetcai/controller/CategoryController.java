package com.cai.violetcai.controller;

import com.alibaba.fastjson.JSON;
import com.cai.violetcai.bean.Article;
import com.cai.violetcai.bean.Category;
import com.cai.violetcai.dao.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    CategoryDao categoryDao;

    //获取文章类别id
    @RequestMapping("/getCateId")
    public int getCateId(int userId, String cateName){
        int categoryId = categoryDao.getCategoryId(userId, cateName);
        return categoryId;
    }

    //获取个人全部分类信息
    @RequestMapping("getCategory")
    public String getAllCategoryByUserId(@RequestParam("userId") int id){
        int numbers = categoryDao.getCategoryCount(id);
        List<Category> category = categoryDao.getAllCategoryByUserId(id);
        HashMap<String, Object> res = new HashMap<>();
        res.put("number", numbers);
        res.put("data", category);
        String res_cateMessage = JSON.toJSONString(res);
        return res_cateMessage;
    }

    //获取个人全部分类信息（降序）
    @RequestMapping("getCategory2")
    public String getAllCategoryByUserId2(@RequestParam("userId") int id){
        int numbers = categoryDao.getCategoryCount(id);
        List<Category> category = categoryDao.getAllCategoryByUserId2(id);
        HashMap<String, Object> res = new HashMap<>();
        res.put("number", numbers);
        res.put("data", category);
        String res_cateMessage = JSON.toJSONString(res);
        return res_cateMessage;
    }

    //新增分类
    @RequestMapping("/addCategory")
    public String addCategory(@RequestBody Category category){
        int i = categoryDao.addCategory(category);
        return i > 0 ? "success":"error";
    }

    //根据分类id查询分类名
    @RequestMapping("/getCateNameById")
    public String getCateNameById(int cateId){
        return categoryDao.getCategoryNameById(cateId);
    }

    //改分类名
    @RequestMapping("/updateCateName")
    public String updateCateName(String cateName, int cateId){
        int i = categoryDao.updateCateName(cateName, cateId);
        return i > 0 ? "success":"error";
    }

    //删除分类
    @Transactional   //事务管理
    @RequestMapping("/deleteCate/{id}/{userId}")
    public String deleteCate(@PathVariable List<Integer> id, @PathVariable int userId){
        int i = categoryDao.deleteMultipleCate(id);
        int j = categoryDao.moveCate(id, userId);
        return j > 0 || i > 0 ? "success":"error";
    }

}
