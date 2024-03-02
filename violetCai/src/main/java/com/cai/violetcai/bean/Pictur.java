package com.cai.violetcai.bean;

import lombok.Data;

/**
 * 图片实体
 */
@Data
public class Pictur {

    private int picturId;
    private String picturUrl;
    private String picturCategory;
    private int userId;

}
