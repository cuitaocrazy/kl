package com.yada.btg.web.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author yn on 2019/12/24.
 * 商户链接实体
 */
@Data
@Entity
@Table(name = "T_B_MER_LINK")
public class MerLink implements Serializable {
    private static final long serialVersionUID = -3023765162457204082L;

    /**
     * 商户id，主键(UUID)
     */
    @Id
    @Column(name = "MER_ID", length = 32)
    private String merId;

    /**
     * 商户标题
     */
    @Column(name = "LIST_TITLE", length = 32)
    private String listTitle;

    /**
     * 商户链接地址
     */
    @Column(name = "MER_LINK", length = 512)
    private String merLink;

    /**
     * 商户序号（取值1-100）
     */
    @Column(name = "MER_ORDER", nullable = false)
    private Integer merOrder;

    /**
     * 更新时间
     */
    @Column(name = "UPDATE_DATE", length = 19, nullable = false)
    private String updateDate;
}
