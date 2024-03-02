package com.cai.violetcai.dao;

import com.cai.violetcai.bean.Pictur;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PicturDao {
    //根据用户id获取图片
    List<Pictur> getAllPicturById(@Param("userId") int id);
    //获取用户图片总数
    int getPicturCounts();
    //添加图片基本数据
    int addPictur(String picturUrl, String picturCategory, int userId);
    //删除相册图片
    int deleteAlbumPictur(List<String> picturUrl);

}
