package com.cai.violetcai.bean;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Announcement {

    public int annId;
    private String content;
    private int userId;
    private LocalDateTime time;

}
