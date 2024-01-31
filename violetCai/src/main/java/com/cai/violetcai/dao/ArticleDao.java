package com.cai.violetcai.dao;

import com.cai.violetcai.bean.Article;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleDao {

    //每页笔记信息(全部)
    public List<Article> getAllArticle(@Param("title") String title, @Param("pageStart") int pageStart, @Param("pageSize") int pageSize);
    //用户个人笔记信息（分页）
    public List<Article> getAllArticleByUserId(@Param("userId") int userId, @Param("title") String title, @Param("pageStart") int pageStart, @Param("pageSize") int pageSize);
    //用户个人笔记信息（分页，根据用户名和类别）
    public List<Article> getAllArticleByUserIdCate(@Param("userId") int userId, @Param("categoryId") int categoryId, @Param("title") String title, @Param("pageStart") int pageStart, @Param("pageSize") int pageSize);
    //查询最近更改的五篇笔记
    public List<Article> getArticleByTime(@Param("userId") int userId);
    //笔记总数
    public int getArticleCounts(@Param("userId") int userId);
    public int getArticleCountsByCate(@Param("userId") int userId, @Param("categoryId") int categoryId);
    public int getArticleCounts2(@Param("articlename") String username);
    //笔记添加
    public int addArticle(Article notes);
    //根据id查询笔记
    public Article getArticleMessage(int id);
    //笔记更新
    public int updateArticle(Article notes);
    public int updateArticleAuthorById(@Param("username") String author, @Param("userId") int userId);
    //笔记删除
    public int deleteArticle(int id);

}
