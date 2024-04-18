package com.cai.violetcai.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendState {

    private int id;
    private int userId;
    private String username;
    private int friId;
    private String friName;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
    private String state;


}
