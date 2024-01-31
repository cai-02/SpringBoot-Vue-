package com.cai.violetcai;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.cai.violetcai.bean.Article;
import com.cai.violetcai.bean.Category;
import com.cai.violetcai.dao.ArticleDao;
import com.cai.violetcai.dao.CategoryDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class VioletCaiApplicationTests {

    @Autowired
    ArticleDao articleDao;

    @Autowired
    CategoryDao categoryDao;

    @Test
    void contextLoads() {
        deleteFile("e5c9861dc3bc4a9f9b288b060bbc7a62");
    }


    public String deleteFile(String flag) {
        String basePath = System.getProperty("user.dir") + "/src/main/resources/files/";
        List<String> fileNames = FileUtil.listFileNames(basePath);   // 获取所有文件名称
        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");

        if (StrUtil.isNotEmpty(fileName)) {
            try {
                Path filePath = Paths.get(basePath, fileName);
                Files.delete(filePath);
                return "文件删除成功";
            } catch (IOException e) {
                // 记录日志或返回适当的错误消息
                return "文件删除失败";
            }
        } else {
            return "未找到匹配的文件";
        }
    }

}
