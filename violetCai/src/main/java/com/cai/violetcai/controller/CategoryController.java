package com.cai.violetcai.controller;

import com.alibaba.fastjson.JSON;
import com.cai.violetcai.bean.Article;
import com.cai.violetcai.bean.Category;
import com.cai.violetcai.dao.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    //新增分类
    @RequestMapping("/addCategory")
    public String addCategory(@RequestBody Category category){
        int i = categoryDao.addCategory(category);
        return i > 0 ? "success":"error";
    }

}
