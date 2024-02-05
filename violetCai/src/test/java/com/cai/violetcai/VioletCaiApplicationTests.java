package com.cai.violetcai;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.cai.violetcai.bean.Article;
import com.cai.violetcai.bean.Category;
import com.cai.violetcai.bean.User;
import com.cai.violetcai.dao.ArticleDao;
import com.cai.violetcai.dao.CategoryDao;
import com.cai.violetcai.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class VioletCaiApplicationTests {

    @Autowired
    ArticleDao articleDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    UserDao userDao;

    @Test
    void contextLoads() {
        List<User> users = userDao.getAllUser("%%", 0, 5);
        for(User user : users){
            System.out.println(user);
        }
    }


}
