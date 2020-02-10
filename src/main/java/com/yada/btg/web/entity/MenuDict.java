package com.yada.btg.web.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author yn on 2019/12/23.
 * 菜单类型实体
 */
@Data
@Entity
@Table(name = "T_B_MENU_DICT")
public class MenuDict implements Serializable {
    private static final long serialVersionUID = 5586098402137777487L;

    /**
     * 菜单类型Key，主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_KEY", length = 1)
    private Long menuKey;

    /**
     * 菜单类型（自定义、列表、富文本框）
     */
    @Column(name = "MENU_VALUE", length = 128)
    private String menuValue;
}
