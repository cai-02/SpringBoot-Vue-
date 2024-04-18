package com.cai.violetcai.dao;

import com.cai.violetcai.bean.Announcement;
import com.cai.violetcai.bean.Liuyan;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiuyanDao {
    //获取全部留言
    List<Liuyan> getAllLiuyan();
    //添加留言
    int addLiuyan(Liuyan liuyan);
    //删除留言
    int deleteLiuyan(@Param("liuyanId") int liuyanId);
    //获取最新的一条留言
    List<Liuyan> getNewLiuyan();

}
