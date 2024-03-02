package com.cai.violetcai.dao;

import com.cai.violetcai.bean.Files;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilesDao {
    //根据用户id获取文件
    List<Files> getAllFilesById(@Param("userId") int id, @Param("fileName") String fileName);
    //获取用户文件总数
    int getFilesCounts();
    //添加文件
    int addFiles(Files files);
    //删除文件
    int deleteFiles(String filesUrl);

}
