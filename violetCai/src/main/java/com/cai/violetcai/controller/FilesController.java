package com.cai.violetcai.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.cai.violetcai.bean.Files;
import com.cai.violetcai.bean.Pictur;
import com.cai.violetcai.dao.FilesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController     //规定所有返回值为String类型
public class FilesController {

    @Autowired
    FilesDao filesDao;

    @RequestMapping("/getFiles")
    public String getFiles(@RequestParam("userId") int id, @RequestParam("fileName") String fileName){
        List<Files> filesList = filesDao.getAllFilesById(id, "%" + fileName + "%");
        HashMap<String, Object> res = new HashMap<>();
        res.put("data", filesList);

        String resJson = JSON.toJSONString(res);    //转换json会自动去掉空字段
        return resJson;
    }

    @RequestMapping("/addFiles")
    public List<String> addFile(@RequestBody Files files){
        files.setFileTime(LocalDateTime.now());
        List<String> filesUrl = new ArrayList<>();
        //System.out.println("files: " + files);
        int i = filesDao.addFiles(files);
        if (i > 0) {
            List<Files> filesList = filesDao.getAllFilesById(files.getUserId(), "");
            for (Files file : filesList){
                filesUrl.add(file.getFileUrl());
            }
            return filesUrl;
        }
        return filesUrl;
    }

    //删除图片
    @RequestMapping("/deleteFile")
    public String deletePictur(@RequestParam("fileUrl") String fileUrl){
        // 获取路径的最后一部分
        String[] segments2 = fileUrl.split("/");
        String lastSegment = segments2[segments2.length - 1];
        String basePath = System.getProperty("user.dir") + "/src/main/resources/files/";
        //String basePath = System.getProperty("user.dir") + "/files/";
        List<String> fileNames = FileUtil.listFileNames(basePath);   // 获取所有文件名称
        String fileName = fileNames.stream().filter(name -> name.contains(lastSegment)).findAny().orElse("");
        if (StrUtil.isNotEmpty(fileName)) {
            Path filePath = Paths.get(basePath, fileName);
            try {
                java.nio.file.Files.delete(filePath); //删除文件
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        int i = filesDao.deleteFiles(fileUrl);  //删除数据库信息
        if (i > 0) {
            return "success";
        } else {
            return "error";
        }
    }

}
