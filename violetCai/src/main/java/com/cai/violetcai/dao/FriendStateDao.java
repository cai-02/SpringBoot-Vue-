package com.cai.violetcai.dao;

import com.cai.violetcai.bean.FriendState;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendStateDao {
    //查询是否有好友请求
    List<FriendState> getFriendMes(int friId);
    //查询非好友用户
    //List<User> getFriendExSelf(int id, String username);
    //添加好友
    int addFriendState(FriendState friendState);
    //删除通知
    int deleteFriendRelate(int userId, int friId);
}
