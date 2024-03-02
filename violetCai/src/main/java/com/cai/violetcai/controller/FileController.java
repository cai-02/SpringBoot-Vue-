package com.cai.violetcai.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
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

    @PostMapping("/uploadAlbum")  //相册上传
    public List<String> upload2(MultipartFile[] file) throws IOException {
        List<String> downloadUrls = new ArrayList<>();

        for (MultipartFile files : file) {
            String originalFilename = files.getOriginalFilename();  //获取源文件名称
            //定义文件的唯一标识
            String flag = IdUtil.fastSimpleUUID();
            String rootFilePath = System.getProperty("user.dir") + "/src/main/resources/images/albumImage/" + flag + "_" + originalFilename;  //获取上传路径
            //String rootFilePath = System.getProperty("user.dir") + "/albumImage/" + flag + "_" + originalFilename;  //获取上传路径
            //把文件写入到上传的路径
            FileUtil.writeBytes(files.getBytes(), rootFilePath);
            downloadUrls.add(ip + ":" + port + "/files/getAlbum/" + flag);
        }
        return downloadUrls;
    }
    @GetMapping("/getAlbum/{flag}")  //相册下载
    public void getFile2(@PathVariable String flag, HttpServletResponse response){
        OutputStream os;   //新建一个输出流对象
        String basePath = System.getProperty("user.dir") + "/src/main/resources/images/albumImage/";
        //String basePath = System.getProperty("user.dir") + "/albumImage";
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

    @PostMapping("/uploadFiles")  //文件上传
    public String upload3(MultipartFile[] file) throws IOException {
        List<String> downloadUrls = new ArrayList<>();
        List<String> fileName = new ArrayList<>();
        List<String> fileSize = new ArrayList<>();

        for (MultipartFile files : file) {
            String originalFilename = files.getOriginalFilename();  //获取源文件名称
            //定义文件的唯一标识
            String flag = IdUtil.fastSimpleUUID();
            String rootFilePath = System.getProperty("user.dir") + "/src/main/resources/files/" + flag + "_" + originalFilename;  //获取上传路径
            //String rootFilePath = System.getProperty("user.dir") + "/albumImage/" + flag + "_" + originalFilename;  //获取上传路径
            //把文件写入到上传的路径
            FileUtil.writeBytes(files.getBytes(), rootFilePath);
            downloadUrls.add(ip + ":" + port + "/files/getFiles/" + flag);
            //获取文件大小
            long fileSizeInBytes = files.getSize();
            double fileSizeInMB = fileSizeInBytes / (1024.0 * 1024.0);
            String formattedFileSize = String.format("%.2fMB", fileSizeInMB);

            fileSize.add(formattedFileSize);
            fileName.add(URLEncoder.encode(originalFilename, StandardCharsets.UTF_8.toString()));
        }
        HashMap<String, Object> res = new HashMap<>();
        res.put("urls", downloadUrls);
        res.put("fileNames", fileName);
        res.put("fileSize", fileSize);
        String res_String = JSON.toJSONString(res);
        //System.out.println("res_String: " + res_String);
        return res_String;
    }
    @GetMapping("/getFiles/{flag}")  //文件下载
    public void getFile3(@PathVariable String flag, HttpServletResponse response){
        OutputStream os;   //新建一个输出流对象
        String basePath = System.getProperty("user.dir") + "/src/main/resources/files/";
        //String basePath = System.getProperty("user.dir") + "/albumImage";
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


    @PostMapping("/noteP")  //文章内图片上传
    public String upload3(MultipartFile file) throws IOException {
        System.out.println("111" + file);
        String originalFilename = file.getOriginalFilename();  //获取源文件名称
        //定义文件的唯一标识
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath = System.getProperty("user.dir") + "/src/main/resources/images/noteImage/" + flag + "_" + originalFilename; //获取上传路径
        //String rootFilePath = System.getProperty("user.dir") + "/headImage/" + flag + "_" + originalFilename;  //获取上传路径
        //把文件写入到上传的路径
        FileUtil.writeBytes(file.getBytes(), rootFilePath);
        return ip + ":" + port + "/files/head/" + flag;
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
