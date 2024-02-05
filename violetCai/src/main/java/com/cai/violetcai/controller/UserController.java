package com.cai.violetcai.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.cai.violetcai.bean.Category;
import com.cai.violetcai.bean.QueryInfo;
import com.cai.violetcai.bean.User;
import com.cai.violetcai.dao.ArticleDao;
import com.cai.violetcai.dao.CategoryDao;
import com.cai.violetcai.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserDao userDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    ArticleDao articleDao;

    @RequestMapping("/alluser")
    public String getUserList(QueryInfo queryInfo){
        //获取最大列表数和当前页编号
        int numbers = userDao.getUserCounts("%" + queryInfo.getQuery() + "%");
        int pageStart = (queryInfo.getPageNum() - 1) * queryInfo.getPageSize();

        List<User> users = userDao.getAllUser("%" + queryInfo.getQuery() + "%", pageStart, queryInfo.getPageSize());
        HashMap<String, Object> res = new HashMap<>();
        res.put("number", numbers);
        res.put("data", users);

        String res_String = JSON.toJSONString(res);
        return res_String;

    }

    @RequestMapping("/userstate")
    public String updateUserState(@RequestParam("id") Integer id, @RequestParam("state") Boolean state){
        int i = userDao.updateState(id, state);
        return i > 0 ? "success":"error";
    }

    @RequestMapping("/adduser")
    public String addUser(@RequestBody User user){
        user.setRole("普通用户");
        user.setState(true);
        user.setHeadimage("http://localhost:9000/files/head/aabfd7dc07704ed4adef568c0797adc2");
        //user.setHeadimage("http://47.108.66.150:9000/files/head/aabfd7dc07704ed4adef568c0797adc2");
        int i = userDao.addUser(user);
        if(i > 0) {
            //创建用户时创建默认分类
            int userId = userDao.getUserIdByName(user.getUsername());
            Category category = new Category();
            category.setUserId(userId);
            category.setCategoryName("默认");
            categoryDao.addCategory(category);
            return "success";
        }else {
            return "error";
        }
    }

    @RequestMapping("/deleteuser")
    public String deleteUser(int id){
        int i = userDao.deleteUser(id);
        return i > 0 ? "success":"error";
    }

    @RequestMapping("/getupdate")
    public String getUpdateUser(int id){
        User user = userDao.getUpdateUser(id);
        String string = JSON.toJSONString(user);
        return string;
    }

    @RequestMapping("/edituser")
    public String editUser(@RequestBody User user){
        int i = userDao.editUser(user);
        //更新笔记作者
        articleDao.updateArticleAuthorById(user.getUsername(), user.getUserId());
        return i > 0 ? "success":"error";
    }

    @RequestMapping("/gethead")
    public String changeImage(@RequestParam("img") String img, @RequestParam("id") int id, String headFlag){
        String basePath = System.getProperty("user.dir") + "/src/main/resources/images/headImage/";
        //String basePath = System.getProperty("user.dir") + "/headImage/";
        List<String> fileNames = FileUtil.listFileNames(basePath);   // 获取所有文件名称
        String fileName = fileNames.stream().filter(name -> name.contains(headFlag)).findAny().orElse("");
        if (StrUtil.isNotEmpty(fileName)) {
            try {
                if (headFlag.equals("aabfd7dc07704ed4adef568c0797adc2")) { //默认头像
                }else{
                    //删除旧头像
                    Path filePath = Paths.get(basePath, fileName);
                    Files.delete(filePath);
                }

                //获取新头像
                int i = userDao.changeImage(img, id);
                return i > 0 ? "success":"error";
            } catch (IOException e) {
                // 记录日志或返回适当的错误消息
                return "文件删除失败";
            }
        }
        return "不会出现的错误";
    }

    @RequestMapping("/uniqueUser")
    @CrossOrigin
    public boolean changeImage(String username){
        // 调用 MyBatis 查询数据库，检查用户名是否已存在
        User existingUser = userDao.getUserByUsername(username);
        return existingUser == null; // 如果不存在，返回 true 表示用户名可用
    }

    //查看权限
    @RequestMapping("/getRole")
    public String changeImage(int userId){
        String role = userDao.getRole(userId);
        return role;
    }

}
