package com.cai.violetcai;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.cai.violetcai.bean.*;
import com.cai.violetcai.dao.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Autowired
    PicturDao picturDao;

    @Autowired
    AnnouncementDao announcementDao;

    @Test
    void contextLoads() {
        Announcement announcement = announcementDao.getAnnouncement();
        HashMap<String, Object> res = new HashMap<>();
        res.put("data", announcement);

        String resJson = JSON.toJSONString(res);    //转换json会自动去掉空字段
        System.out.println(resJson);
    }

    public String getPictur(@RequestParam("userId") int id){
        List<Pictur> picturList = picturDao.getAllPicturById(id);
        HashMap<String, Object> res = new HashMap<>();
        res.put("data", picturList);

        String resJson = JSON.toJSONString(res);    //转换json会自动去掉空字段
        return resJson;
    }


}
