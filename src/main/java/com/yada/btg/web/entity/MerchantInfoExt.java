package com.yada.btg.web.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Description:商户扩展信息实体
 *
 * @author songpeijiang
 * @since 2020/1/6
 */
@Data
@Entity
@Table(name = "T_B_MERCHANT_INFO_EXT")
public class MerchantInfoExt implements Serializable {

    private static final long serialVersionUID = 1096003863911981747L;
    /**
     * 主键
     */
    @Id
    @Column(name = "MERCHANT_INFO_EXT_ID", unique = true, nullable = false, length = 32)
    private String merchantInfoExtId;
    /**
     * 商户信息的顺序标识
     */
    @Column(name = "MER_IMG_ORDER", columnDefinition = "double(8,3)")
    private String merImgOrder;
    /**
     * 商品介绍图片
     */
    @Column(name = "MER_IMG", length = 512)
    private String merImg;
    /**
     * 商户介绍文字
     */
    @Column(name = "TEXT_INTRODUCTION", length = 512)
    private String textIntroduction;

    @ManyToOne
    @JoinColumn(name = "MER_INFO_ID")
    private MerchantInfo merchantInfo;

}
