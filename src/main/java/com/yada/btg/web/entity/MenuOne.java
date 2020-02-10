package com.yada.btg.web.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yn on 2019/12/23.
 * 一级菜单实体
 */
@Data
@Entity
@Table(name = "T_B_MENU_ONE")
public class MenuOne implements Serializable {
    private static final long serialVersionUID = -4442738311483532717L;

    /**
     * 菜单id，主键(UUID)
     */
    @Id
    @Column(name = "MENU_ID", length = 32)
    private String menuId;

    /**
     * 菜单名称
     */
    @Column(name = "MENU_NAME", length = 8)
    private String menuName;

    /**
     * 是否作为头部的一级菜单（0-否；1-是）
     */
    @Column(name = "HEADER_FLAG", length = 1)
    private Integer headerFlag;

    /**
     * 菜单权重(1-100)【首页是最小值】
     */
    @Column(name = "MENU_WEIGHT")
    private Integer menuWeight;

    /**
     * 简介
     */
    @Column(name = "MENU_INTRODUCTION", length = 512)
    private String menuIntroduction;

    /**
     * 对应的二级菜单
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "T_B_MENU_ONE_TWO",
            joinColumns = @JoinColumn(name = "MENU_ONE", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)),
            inverseJoinColumns = @JoinColumn(name = "MENU_TWO", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"MENU_ONE", "MENU_TWO"})})
    private List<MenuTwo> menuTwoList = new ArrayList<>();
}
