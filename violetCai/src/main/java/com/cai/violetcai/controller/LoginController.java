package com.cai.violetcai.controller;

import com.alibaba.fastjson.JSON;
import com.cai.violetcai.bean.User;
import com.cai.violetcai.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController     //规定所有返回值为String类型
public class LoginController {

    @Autowired
    UserDao userDao;

    @RequestMapping("/login")
    public String login(@RequestBody User user){
        String flag = "error";
        User us = userDao.getUserByMessage(user.getUsername(), user.getPassword());
        HashMap<String, Object> res = new HashMap<>();
        if(us != null){
            flag = "ok";
            res.put("userId", us.getUserId());
            res.put("headimage", us.getHeadimage());
        }

        res.put("flag", flag);
        res.put("user", user);
        String resJson = JSON.toJSONString(res);    //转换json会自动去掉空字段
        return resJson;
    }

}
