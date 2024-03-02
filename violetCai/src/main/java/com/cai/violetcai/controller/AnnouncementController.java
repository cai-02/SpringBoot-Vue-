package com.cai.violetcai.controller;

import com.alibaba.fastjson.JSON;
import com.cai.violetcai.bean.*;
import com.cai.violetcai.dao.AnnouncementDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController     //规定所有返回值为String类型
public class AnnouncementController {

    @Autowired
    AnnouncementDao announcementDao;

    //后台获取全部公告
    @RequestMapping("/getAllAnn")
    public String getAllAnn(@RequestParam("pageStart") Integer pageNum, @RequestParam("pageSize") Integer pageSize){
        //获取最大列表数和当前页编号
        int numbers = announcementDao.getAnnCounts();
        int pageStart = (pageNum - 1) * pageSize;

        List<Announcement> announcements = announcementDao.getAllAnn(pageStart, pageSize);
        HashMap<String, Object> res = new HashMap<>();
        res.put("number", numbers);
        res.put("data", announcements);

        String res_String = JSON.toJSONString(res);
        return res_String;

    }

    //获取最新一条公告
    @RequestMapping("/getAnn")
    public String getAnn(){
        Announcement announcement = announcementDao.getAnnouncement();
        HashMap<String, Object> res = new HashMap<>();
        res.put("data", announcement);

        String resJson = JSON.toJSONString(res);    //转换json会自动去掉空字段
        return resJson;
    }

    //添加公告
    @RequestMapping("/addAnn")
    public String addArticle(@RequestBody Announcement ann){
        ann.setTime(LocalDateTime.now());
        int i = announcementDao.addAnn(ann);
        return i > 0 ? "success":"error";
    }

    @RequestMapping("/deleteAnn")
    public String deleteAnn(int annId){
        int i = announcementDao.deleteAnn(annId);
        return i > 0 ? "success":"error";
    }

}
