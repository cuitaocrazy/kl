package com.yada.btg.web.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Description: 数字字典信息
 *
 * @author songpeijiang
 * @since 2020/1/7
 */
@Data
@Entity
@Table(name = "T_B_DICT_ITEM")
public class DictInfo implements Serializable {
    private static final long serialVersionUID = 845500002025714734L;

    /**
     * 字典id
     */
    @Id
    @Column(name = "ITEM_ID", unique = true, nullable = false, length = 32)
    private String itemId;
    /**
     * 字典key键
     */
    @Column(name = "ITEM_KEY", nullable = false, length = 12)
    private String itemKey;
    /**
     * 字典类型
     */
    @Column(name = "ITEM_TYPE", length = 16)
    private String itemType;
    /**
     * 字典值
     */
    @Column(name = "ITEM_VALUE", length = 128)
    private String itemValue;
}
