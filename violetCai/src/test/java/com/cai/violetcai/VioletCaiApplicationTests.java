package com.cai.violetcai;

import com.alibaba.fastjson.JSON;
import com.cai.violetcai.bean.Article;
import com.cai.violetcai.bean.Category;
import com.cai.violetcai.dao.ArticleDao;
import com.cai.violetcai.dao.CategoryDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;

@SpringBootTest
class VioletCaiApplicationTests {

    @Autowired
    ArticleDao articleDao;

    @Autowired
    CategoryDao categoryDao;

    @Test
    void contextLoads() {
        int numbers = articleDao.getArticleCountsByCate("violet", 7);
        int pageStart = (1 - 1) * 10;

        List<Article> articles = articleDao.getAllArticleByAuthorCate("violet", 7, "%%", pageStart, 10);
        HashMap<String, Object> res = new HashMap<>();
        res.put("number", numbers);
        res.put("data", articles);
        String res_String = JSON.toJSONString(res);
        System.out.println(res_String);
    }

}
