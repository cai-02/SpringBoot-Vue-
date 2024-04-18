package com.cai.violetcai.controller;

import com.alibaba.fastjson.JSON;
import com.cai.violetcai.bean.Friend;
import com.cai.violetcai.bean.FriendState;
import com.cai.violetcai.bean.User;
import com.cai.violetcai.dao.FriendStateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController     //规定所有返回值为String类型
public class FriendStateController {

    @Autowired
    FriendStateDao friendStateDao;

    //获取全部好友请求
    @RequestMapping("/getFriendMes")
    public String getFriendMes(int friId){
        List<FriendState> friend = friendStateDao.getFriendMes(friId);
        HashMap<String, Object> res = new HashMap<>();
        res.put("data", friend);

        String res_String = JSON.toJSONString(res);
        return res_String;
    }

    //添加好友状态
    @RequestMapping("/addFriendState")
    public String addFriendState(@RequestBody FriendState friendState){
        friendState.setTime(LocalDateTime.now());
        int i = friendStateDao.addFriendState(friendState);
        return i > 0 ? "success":"error";
    }

    //同意或拒绝后删除通知
    @RequestMapping("/deleteFriendRelate")
    public String deleteFriendRelate(int userId, int friId){
        int i = friendStateDao.deleteFriendRelate(userId, friId);
        return i > 0 ? "success":"error";
    }

}
