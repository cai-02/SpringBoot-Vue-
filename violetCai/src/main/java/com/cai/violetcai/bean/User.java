package com.cai.violetcai.bean;

import lombok.Data;

/**
 * 用户实体
 */
@Data
public class User {

    private int userId;
    private String username;
    private String password;
    private String email;
    private String role;
    private boolean state;
    private String headimage;

}
