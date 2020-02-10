package com.yada.btg.web.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author yn on 2019/12/23.
 * 菜单图片实体
 */
@Data
@Entity
@Table(name = "T_B_MENU_IMG_EXT")
public class MenuImgExt implements Serializable {
    private static final long serialVersionUID = 1866620003501402087L;

    /**
     * 图片id，主键(UUID)
     */
    @Id
    @Column(name = "IMG_ID", length = 32,nullable = false)
    private String imgId;

    /**
     * 一级菜单id
     */
    @Column(name = "MENU_ONE", length = 32,nullable = false)
    private String menuOne;

    /**
     * 二级菜单id
     */
    @Column(name = "MENU_TWO", length = 32)
    private String menuTwo;

    /**
     * 菜单图片地址
     */
    @Column(name = "IMG_PATH", length = 512)
    private String imgPath;

    /**
     * 图片链接地址
     */
    @Column(name = "IMG_LINK", length = 512)
    private String imgLink;

    /**
     * 图片说明
     */
    @Column(name = "IMG_INTRODUCTION", length = 512)
    private String imgIntroduction;

    /**
     * 更新日期
     */
    @Column(name = "UPDATE_DATE", length = 19, nullable = false)
    private String updateDate;
}
