package com.yada.btg.web.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author yn on 2019/12/27.
 * 业务介绍实体
 */
@Data
@Entity
@Table(name = "T_B_BUSINESS_INFO")
public class BusinessInfo implements Serializable {
    private static final long serialVersionUID = 5592286923398073749L;

    /**
     * 主键(UUID)
     */
    @Id
    @Column(name = "BUSINESS_ID", length = 32)
    private String businessId;

    /**
     * 业务标题
     */
    @Column(name = "BUSINESS_NAME", length = 8)
    private String businessName;

    /**
     * 业务图标
     */
    @Column(name = "BUSINESS_IMG", length = 512)
    private String businessImg;

    /**
     * 对应的一级菜单
     */
    @OneToOne(targetEntity = MenuOne.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "MENU_ONE", referencedColumnName = "MENU_ID")
    private MenuOne menuOne;

    /**
     * 对应的二级菜单
     */
    @OneToOne(targetEntity = MenuTwo.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "MENU_TWO", referencedColumnName = "MENU_ID")
    private MenuTwo menuTwo;

    /**
     * 菜单权重（排序用，取值范围1-100）
     */
    @Column(name = "BUSINESS_WEIGHT")
    private Integer businessWeight;

    /**
     * 是否展示在首页（只能是4个）
     */
    @Column(name = "SHOW_FLAG", length = 1)
    private Integer showFlag;

    /**
     * 业务简介
     */
    @Column(name = "BUSINESS_INTRODUCTION", length = 512)
    private String businessIntroduction;

    /**
     * 更新日期
     */
    @Column(name = "UPDATE_DATE", length = 19,nullable = false)
    private String updateDate;
}
