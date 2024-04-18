package com.cai.violetcai.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.cai.violetcai.bean.Article;
import com.cai.violetcai.bean.Category;
import com.cai.violetcai.bean.Pictur;
import com.cai.violetcai.bean.User;
import com.cai.violetcai.dao.PicturDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController     //规定所有返回值为String类型
public class PicturController {

    @Autowired
    PicturDao picturDao;

    @RequestMapping("/getPictur")
    public String getPictur(@RequestParam("userId") int id){
        List<Pictur> picturList = picturDao.getAllPicturById(id);
        HashMap<String, Object> res = new HashMap<>();
        res.put("data", picturList);

        String resJson = JSON.toJSONString(res);    //转换json会自动去掉空字段
        return resJson;
    }

    @RequestMapping("/addPictur")
    public List<String> addPicturs(String picturUrl, int userId){
        List<String> imgUrl = new ArrayList<>();
        int i = picturDao.addPictur(picturUrl, "默认", userId);
        if (i > 0) {
            List<Pictur> picturList = picturDao.getAllPicturById(userId);
            for (Pictur pic : picturList){
                imgUrl.add(pic.getPicturUrl());
            }
            return imgUrl;
        }
        return imgUrl;
    }

    @Value("${service.albumAdd}")
    private String albumAdd;
    //删除图片
    @RequestMapping("/deletePictur")
    public String deletePictur(@RequestBody List<String> picturUrl){
        // 使用 StringJoiner 将列表元素连接为一个字符串，以逗号分隔
        String concatenatedString = String.join(",", picturUrl);
        // 使用 split 方法处理字符串
        String[] segments = concatenatedString.split(",");
        // 设置一个标志，用于指示是否找到匹配项
        boolean foundMatch = false;
        for(String element : segments){
            // 获取路径的最后一部分
            String[] segments2 = element.split("/");
            String lastSegment = segments2[segments2.length - 1];
            //String basePath = System.getProperty("user.dir") + "/src/main/resources/images/albumImage/";
            String basePath = System.getProperty("user.dir") + albumAdd;
            List<String> fileNames = FileUtil.listFileNames(basePath);   // 获取所有文件名称
            String fileName = fileNames.stream().filter(name -> name.contains(lastSegment)).findAny().orElse("");
            if (StrUtil.isNotEmpty(fileName)) {
                Path filePath = Paths.get(basePath, fileName);
                try {
                    Files.delete(filePath); //删除图片
                    foundMatch = true;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        int i = picturDao.deleteAlbumPictur(picturUrl);  //删除数据库信息
        // 在循环之外判断是否找到匹配项，如果找到就返回
        if (foundMatch && i > 0) {
            return "success";
        } else {
            return "error";  // 或者返回其他值，表示未找到匹配项
        }
    }

}
