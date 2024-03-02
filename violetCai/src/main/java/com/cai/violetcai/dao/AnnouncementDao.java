package com.cai.violetcai.dao;

import com.cai.violetcai.bean.Announcement;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementDao {
    //获取最新公告
    Announcement getAnnouncement();
    //获取全部
    List<Announcement> getAllAnn(@Param("pageStart") int pageStart, @Param("pageSize") int pageSize);
    //获取总数
    int getAnnCounts();
    //添加公告
    int addAnn(Announcement ann);
    //删除
    int deleteAnn(@Param("annId") int annId);

}
