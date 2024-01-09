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
    //查询点击量最多的五篇文章
    public List<Article> getArticleByHeat();
    //文章总数
    public int getArticleCounts(@Param("author") String author);
    public int getArticleCounts2(@Param("articlename") String username);
    //文章添加
    public int addArticle(Article notes);
    //根据id查询文章
    public Article getArticleMessage(int id);
    //点击量增加
    public int changeHeat(int heat, int id);

}
