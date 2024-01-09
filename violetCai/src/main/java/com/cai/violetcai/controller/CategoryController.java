package com.cai.violetcai.controller;

import com.cai.violetcai.dao.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
