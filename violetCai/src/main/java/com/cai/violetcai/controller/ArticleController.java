package com.cai.violetcai.controller;

import com.alibaba.fastjson.JSON;
import com.cai.violetcai.bean.Article;
import com.cai.violetcai.bean.QueryInfo;
import com.cai.violetcai.bean.User;
import com.cai.violetcai.dao.ArticleDao;
import com.cai.violetcai.dao.CategoryDao;
import com.cai.violetcai.dao.UserDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public String getArticleList(int userId, String title, @RequestParam("pageStart") Integer pageNum, Integer pageSize){
        //获取最大列表数和当前页编号
        int numbers = articleDao.getArticleCounts(userId);
        int pageStart = (pageNum - 1) * pageSize;

        List<Article> articles = articleDao.getAllArticleByUserId(userId, "%" + title + "%", pageStart, pageSize);
        HashMap<String, Object> res = new HashMap<>();
        res.put("number", numbers);
        res.put("data", articles);
        //System.out.println(userId + " " + title + " " + pageStart + " " + pageSize + "  //" + articles);
        String res_String = JSON.toJSONString(res);
        return res_String;
    }

    @RequestMapping("/getArticleByHeat")
    public String getArticleList2(int userId){
        //获取最大列表数和当前页编号
        int numbers = articleDao.getArticleCounts(userId);
        //List<Article> articles = articleDao.getArticleByHeat();
        HashMap<String, Object> res = new HashMap<>();
        res.put("number", numbers);
        //res.put("data", articles);

        String res_String = JSON.toJSONString(res);
        return res_String;
    }

    //保存笔记
    @RequestMapping("/addArticle")
    public String addArticle(@RequestBody Article notes){
        notes.setTime(LocalDateTime.now());
        notes.setPubTime(LocalDateTime.now());
        int categoryId = categoryDao.getCategoryId(notes.getUserId(), notes.getCategoryName());
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

    //查询最近更改的文章
    @RequestMapping("/getArticleByTime")
    public String getArticleByTime(@RequestParam("userId") int userId){
        List<Article> articles = articleDao.getArticleByTime(userId);
        HashMap<String, Object> res = new HashMap<>();
        res.put("data", articles);
        String res_String = JSON.toJSONString(res);
        return res_String;
    }

    //笔记更新
    @RequestMapping("/updateArticle")
    public String updateArticle(@RequestBody Article notes){
        int categoryId = categoryDao.getCategoryId(notes.getUserId(), notes.getCategoryName());
        notes.setCategoryId(categoryId);
        int i = articleDao.updateArticle(notes);
        return i > 0 ? "success":"error";
    }

    //更新分类
    @RequestMapping("/updateArticleCate")
    public String updateArticleCate(@RequestParam("cateName") String cateName, @RequestParam("categoryId") int categoryId, @RequestParam("noteId") int noteId){
        int i = articleDao.updateArticleCate(cateName, categoryId, noteId);
        return i > 0 ? "success":"error";
    }

    //获取全部个人笔记数据（根据用户及类别）
    @RequestMapping("/getArticleByCate")
    public String getArticleList(int userId, int categoryId, String title, @RequestParam("pageStart") Integer pageNum, Integer pageSize){
        //获取最大列表数和当前页编号
        int numbers = articleDao.getArticleCountsByCate(userId, categoryId);
        int pageStart = (pageNum - 1) * pageSize;
        //获取类别名
        String categoryName = categoryDao.getCategoryNameById(categoryId);

        List<Article> articles = articleDao.getAllArticleByUserIdCate(userId, categoryId, "%" + title + "%", pageStart, pageSize);
        HashMap<String, Object> res = new HashMap<>();
        res.put("number", numbers);
        res.put("data", articles);
        res.put("categoryName", categoryName);
        String res_String = JSON.toJSONString(res);
        return res_String;
    }

    //删除笔记
    @RequestMapping("/deleteArticle")
    public String deleteArticle(@Param("noteId") int noteId) {
        int s = articleDao.deleteArticle(noteId);
        return s > 0 ? "success":"error";
    }

}
