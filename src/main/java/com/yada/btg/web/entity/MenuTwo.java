package com.yada.btg.web.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author yn on 2019/12/23.
 * 二级菜单实体
 */
@Data
@Entity
@Table(name = "T_B_MENU_TWO")
public class MenuTwo implements Serializable {
    private static final long serialVersionUID = 5544894743270125086L;

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
     * 所属类型
     */
    @ManyToOne(targetEntity = MenuDict.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "MENU_TYPE", referencedColumnName = "MENU_KEY")
    private MenuDict menuType;

    /**
     * 所属栏目（当类型为列表时必输）
     */
    @ManyToOne(targetEntity = ColumnInfo.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "COLUMN_TYPE", referencedColumnName = "COLUMN_ID")
    private ColumnInfo columnType;

    /**
     * 大字段（当类型为富文本框时必输）
     */
    @Lob
    @Column(name = "CONTENT")
    private String content;

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
     * 跳转路径
     */
    @Column(name = "REDIRECT_PATH", length = 512, nullable = false)
    private String redirectPath;

    /**
     * 更新日期
     */
    @Column(name = "UPDATE_DATE", length = 19, nullable = false)
    private String updateDate;

    /**
     * 对应的一级菜单
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "T_B_MENU_ONE_TWO",
            joinColumns = @JoinColumn(name = "MENU_TWO", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)),
            inverseJoinColumns = @JoinColumn(name = "MENU_ONE", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"MENU_ONE", "MENU_TWO"})})
    private List<MenuOne> menuOneList;
}
