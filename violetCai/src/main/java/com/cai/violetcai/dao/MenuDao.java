package com.cai.violetcai.dao;

import com.cai.violetcai.bean.MainMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuDao {
    public List<MainMenu> getMenus(@Param("userId") int userId);
}
