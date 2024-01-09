package com.cai.violetcai.controller;

import com.alibaba.fastjson.JSON;
import com.cai.violetcai.bean.Article;
import com.cai.violetcai.bean.QueryInfo;
import com.cai.violetcai.bean.User;
import com.cai.violetcai.dao.ArticleDao;
import com.cai.violetcai.dao.CategoryDao;
import com.cai.violetcai.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class ArticleController {

    @Autowired
    ArticleDao articleDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    UserDao userDao;

    @RequestMapping("/allArticle")
    public String getAllArticleList(QueryInfo queryInfo){
        //获取最大列表数和当前页编号
        int numbers = articleDao.getArticleCounts2("%" + queryInfo.getQuery() + "%");
        int pageStart = (queryInfo.getPageNum() - 1) * queryInfo.getPageSize();

        List<Article> articles = articleDao.getAllArticle("%" + queryInfo.getQuery() + "%", pageStart, queryInfo.getPageSize());
        HashMap<String, Object> res = new HashMap<>();
        res.put("number", numbers);
        res.put("data", articles);

        String res_String = JSON.toJSONString(res);
        return res_String;
    }

    //获取全部个人笔记数据
    @RequestMapping("/getArticle")
    public String getArticleList(String author, String title, @RequestParam("pageStart") Integer pageNum, Integer pageSize){
        //获取最大列表数和当前页编号
        int numbers = articleDao.getArticleCounts(author);
        int pageStart = (pageNum - 1) * pageSize;

        List<Article> articles = articleDao.getAllArticleByAuthor(author, "%" + title + "%", pageStart, pageSize);
        HashMap<String, Object> res = new HashMap<>();
        res.put("number", numbers);
        res.put("data", articles);
        String res_String = JSON.toJSONString(res);
        return res_String;
    }

    @RequestMapping("/getArticleByHeat")
    public String getArticleList2(String author){
        //获取最大列表数和当前页编号
        int numbers = articleDao.getArticleCounts(author);
        //List<Article> articles = articleDao.getArticleByHeat();
        HashMap<String, Object> res = new HashMap<>();
        res.put("number", numbers);
        //res.put("data", articles);

        String res_String = JSON.toJSONString(res);
        return res_String;
    }

    //保持笔记
    @RequestMapping("/addArticle")
    public String addArticle(@RequestBody Article notes){
        int userId = userDao.getUserIdByName(notes.getAuthor());
        int categoryId = categoryDao.getCategoryId(userId, notes.getCategoryName());
        notes.setCategoryId(categoryId);
        int i = articleDao.addArticle(notes);
        return i > 0 ? "success":"error";
    }

    //根据id获取文章信息
    @RequestMapping("/articleMessage")
    public String articleMessage(@RequestParam("id") int id){
        Article articleMessage = articleDao.getArticleMessage(id);
        String res_articleMessage = JSON.toJSONString(articleMessage);
        return res_articleMessage;
    }

}
