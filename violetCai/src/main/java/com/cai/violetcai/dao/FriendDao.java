package com.cai.violetcai.dao;

import com.cai.violetcai.bean.Friend;
import com.cai.violetcai.bean.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendDao {
    //获取好友信息
    List<Friend> getFriend(int id);
    //查询非好友用户
    List<User> getFriendExSelf(int id, String username);
    //添加好友关系
    int addFriendRelate(int userId, String username, int friId);
    //删除好友
    //int deleteLiuyan(@Param("liuyanId") int liuyanId);
}
