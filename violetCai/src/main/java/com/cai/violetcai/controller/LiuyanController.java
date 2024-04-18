package com.cai.violetcai.controller;

import com.alibaba.fastjson.JSON;
import com.cai.violetcai.bean.Liuyan;
import com.cai.violetcai.dao.LiuyanDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController     //规定所有返回值为String类型
public class LiuyanController {

    @Autowired
    LiuyanDao liuyanDao;

    //获取全部留言
    @RequestMapping("/getAllLiuyan")
    public String getAllLiuyan(){
        List<Liuyan> liuyan = liuyanDao.getAllLiuyan();
        HashMap<String, Object> res = new HashMap<>();
        res.put("data", liuyan);

        String res_String = JSON.toJSONString(res);
        return res_String;
    }

    //获取最新一条留言
    @RequestMapping("/getNewLiuyan")
    public String getNewLiuyan(){
        List<Liuyan> liuyan = liuyanDao.getNewLiuyan();
        HashMap<String, Object> res = new HashMap<>();
        res.put("data", liuyan);

        String res_String = JSON.toJSONString(res);
        return res_String;
    }

    //添加留言
    @RequestMapping("/addLiuyan")
    public String addArticle(@RequestBody Liuyan liuyan){
        liuyan.setLiuyanTime(LocalDateTime.now());
        int i = liuyanDao.addLiuyan(liuyan);
        return i > 0 ? "success":"error";
    }
//
//    //删除留言
//    @RequestMapping("/deleteAnn")
//    public String deleteAnn(int annId){
//        int i = announcementDao.deleteAnn(annId);
//        return i > 0 ? "success":"error";
//    }

}
