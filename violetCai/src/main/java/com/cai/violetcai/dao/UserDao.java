package com.cai.violetcai.dao;

import com.cai.violetcai.bean.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {

    //返回登录用户信息
    public User getUserByMessage(@Param("username") String username, @Param("password")String password);
    //返回每页用户信息
    public List<User> getAllUser(@Param("username") String username, @Param("pageStart") int pageStart, @Param("pageSize") int pageSize);
    public int getUserCounts(@Param("username") String username);
    public int updateState(Integer id, Boolean state);
    public int addUser(User user);
    public int deleteUser(int id);
    public  User getUpdateUser(int id);
    public int editUser(User user);
    //用户名获取用户id
    public int getUserIdByName(@Param("username") String username);
    //修改头像方法
    public int changeImage(@Param("headImg") String headImg, @Param("id") int id);

}
