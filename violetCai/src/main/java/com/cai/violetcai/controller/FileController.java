package com.cai.violetcai.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {
    @Value("${server.port}")
    private String port;

    private static final String ip = "http://localhost";
    //private static final String ip = "http://47.108.66.150";

    /**
     * 上传接口
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")  //头像上传
    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();  //获取源文件名称
        //定义文件的唯一标识
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath = System.getProperty("user.dir") + "/src/main/resources/images/headImage/" + flag + "_" + originalFilename;  //获取上传路径
        //String rootFilePath = System.getProperty("user.dir") + "/headImage/" + flag + "_" + originalFilename;  //获取上传路径
        //把文件写入到上传的路径
        FileUtil.writeBytes(file.getBytes(), rootFilePath);
        return ip + ":" + port + "/files/head/" + flag;
    }

    @PostMapping("/noteP")  //文章内图片上传
    public String upload2(MultipartFile file) throws IOException {
        System.out.println("111" + file);
        String originalFilename = file.getOriginalFilename();  //获取源文件名称
        //定义文件的唯一标识
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath = System.getProperty("user.dir") + "/src/main/resources/images/noteImage/" + flag + "_" + originalFilename;  //获取上传路径
        //String rootFilePath = System.getProperty("user.dir") + "/headImage/" + flag + "_" + originalFilename;  //获取上传路径
        //把文件写入到上传的路径
        FileUtil.writeBytes(file.getBytes(), rootFilePath);
        return ip + ":" + port + "/files/head/" + flag;
    }


    /**
     * 下载接口
     * @param flag
     * @param response
     */
    @GetMapping("/head/{flag}")  //头像下载
    public void getFile(@PathVariable String flag, HttpServletResponse response){
        OutputStream os;   //新建一个输出流对象
        String basePath = System.getProperty("user.dir") + "/src/main/resources/images/headImage/";
        //String basePath = System.getProperty("user.dir") + "/headImage";
        List<String> fileNames = FileUtil.listFileNames(basePath);   //获取所有文件名称
        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");  //找到跟参数一致的文件
        try{
            if(StrUtil.isNotEmpty(fileName)){
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(basePath + File.separator + fileName);  //通过文件路径读取文件字节流
                os = response.getOutputStream();   //通过输出流返回文件
                os.write(bytes);
                os.flush();
                os.close();
            }
        }catch (Exception e){
            System.out.println("文件下载失败");
        }
    }

//    @GetMapping("/cover/{flag}")  //笔记背景下载
//    public void getCoverFile(@PathVariable String flag, HttpServletResponse response){
//        OutputStream os;   //新建一个输出流对象
//        //String basePath = System.getProperty("user.dir") + "/src/main/resources/cover/";
//        String basePath = System.getProperty("user.dir") + "/headImage/";
//        List<String> fileNames = FileUtil.listFileNames(basePath);   //获取所有文件名称
//        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");  //找到跟参数一致的文件
//        try{
//            if(StrUtil.isNotEmpty(fileName)){
//                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
//                response.setContentType("application/octet-stream");
//                byte[] bytes = FileUtil.readBytes(basePath + fileName);  //通过文件路径读取文件字节流
//                os = response.getOutputStream();   //通过输出流返回文件
//                os.write(bytes);
//                os.flush();
//                os.close();
//            }
//        }catch (Exception e){
//            System.out.println("文件下载失败");
//        }
//    }

}
