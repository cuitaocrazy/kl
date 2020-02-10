package com.yada.btg.web.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yn on 2019/12/27.
 * 栏目类实体
 */
@Data
@Entity
@Table(name = "T_B_COLUMN_INFO")
public class ColumnInfo implements Serializable {
    private static final long serialVersionUID = -6566221451423611595L;

    /**
     * 栏目ID，主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COLUMN_ID", length = 3)
    private Long columnId;

    /**
     * 栏目名称
     */
    @Column(name = "COLUMN_NAME", length = 128)
    private String columnName;

    /**
     * 栏目对应的二级菜单
     */
    @OneToMany(targetEntity = MenuTwo.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "COLUMN_TYPE", referencedColumnName = "COLUMN_ID")
    private List<MenuTwo> menuTwo;

    /**
     * 栏目对应的文字列表
     */
    @Transient
    private List<TextInfo> textInfoList = new ArrayList<>();
}
