package com.cai.violetcai.controller;

import com.alibaba.fastjson.JSON;
import com.cai.violetcai.bean.Friend;
import com.cai.violetcai.bean.User;
import com.cai.violetcai.dao.FriendDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController     //规定所有返回值为String类型
public class FriendController {

    @Autowired
    FriendDao friendDao;

    //获取全部好友信息
    @RequestMapping("/getFriend")
    public String getFriend(int id){
        List<Friend> friend = friendDao.getFriend(id);
        HashMap<String, Object> res = new HashMap<>();
        res.put("data", friend);

        String res_String = JSON.toJSONString(res);
        return res_String;
    }

    //获取全部非好友用户
    @RequestMapping("/getFriendExSelf")
    public String getFriendExSelf(int id, String username){
        List<User> friend = friendDao.getFriendExSelf(id, "%" + username + "%");
        HashMap<String, Object> res = new HashMap<>();
        res.put("data", friend);

        String res_String = JSON.toJSONString(res);
        return res_String;
    }

    //添加好友关系
    @RequestMapping("/addFriendRelate")
    public String addFriendRelate(int userId, String username, int friId){
        int i = friendDao.addFriendRelate(userId, username, friId);
        return i > 0 ? "success":"error";
    }

//    //删除留言
//    @RequestMapping("/deleteAnn")
//    public String deleteAnn(int annId){
//        int i = announcementDao.deleteAnn(annId);
//        return i > 0 ? "success":"error";
//    }

}
