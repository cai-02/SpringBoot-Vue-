package com.cai.violetcai.bean;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Files {

    private int fileId;
    private String fileName;
    private String fileSize;
    private LocalDateTime fileTime;
    private int userId;
    private String fileUrl;

}
