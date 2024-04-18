package com.cai.violetcai.bean;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Liuyan {

    private int liuyanId;
    private String liuyanContent;
    private int userId;
    private LocalDateTime liuyanTime;
    private String headImage;

}
