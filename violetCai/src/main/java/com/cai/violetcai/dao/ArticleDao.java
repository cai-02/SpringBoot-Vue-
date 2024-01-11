package com.cai.violetcai.dao;

import com.cai.violetcai.bean.Article;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleDao {

    //每页笔记信息(全部)
    public List<Article> getAllArticle(@Param("title") String title, @Param("pageStart") int pageStart, @Param("pageSize") int pageSize);
    //用户个人笔记信息
    public List<Article> getAllArticleByAuthor(@Param("author") String author, @Param("title") String title, @Param("pageStart") int pageStart, @Param("pageSize") int pageSize);
    //查询点击量最多的五篇笔记
    public List<Article> getArticleByHeat();
    //笔记总数
    public int getArticleCounts(@Param("author") String author);
    public int getArticleCounts2(@Param("articlename") String username);
    //笔记添加
    public int addArticle(Article notes);
    //根据id查询笔记
    public Article getArticleMessage(int id);
    //笔记更新
    public int updateArticle(Article notes);
    //int noteId, String title, int categoryId, String categoryName, String content, String time

}
