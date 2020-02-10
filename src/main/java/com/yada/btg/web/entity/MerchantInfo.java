package com.yada.btg.web.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author yn on 2019/12/24.
 * 特约商户主表实体
 */
@Data
@Entity
@Table(name = "T_B_MERCHANT_INFO")
public class MerchantInfo implements Serializable {
    private static final long serialVersionUID = 2313334579538677706L;

    /**
     * 主键(UUID)
     */
    @Id
    @Column(name = "MER_INFO_ID", length = 32)
    private String merInfoId;

    /**
     * 商户标题
     */
    @Column(name = "MER_TITLE", length = 32)
    private String merTitle;

    /**
     * 商户主图片
     */
    @Column(name = "MER_MAIN_IMG", length = 512)
    private String merMainImg;

    /**
     * 电话
     */
    @Column(name = "TELEPHONE", length = 16)
    private String telephone;

    /**
     * 地址
     */
    @Column(name = "ADDRESS", length = 256)
    private String address;

    /**
     * 优惠信息
     */
    @Column(name = "DISCOUNT_INFO", length = 4000)
    private String discountInfo;

    /**
     * 特别声明
     */
    @Column(name = "IMPORTANT_CLAUSE", length = 4000)
    private String importantClause;

    /**
     * 其他描述（没有特殊颜色的结尾）
     */
    @Column(name = "OTHER_DESC", length = 4000)
    private String otherDesc;

    /**
     * 是否为特惠商户标识，默认为0（0-非特惠商户；1-特惠商户）
     */
    @Column(name = "DISCOUNT_MER_FLAG", columnDefinition = "char(1)")
    private String discountMerFlag;

    /**
     * 所属区域
     */
    @Column(name = "AREA", length = 16)
    private String area;

    /**
     * 商户类型
     */
    @Column(name = "MER_TYPE", length = 16)
    private String merType;

    /**
     * 卡类型
     */
    @Column(name = "CARD_TYPE", length = 512)
    private String cardType;

    /**
     * 启用标识（0-停用；1-启用）
     */
    @Column(name = "ENABLE_FLAG", columnDefinition = "char(1)")
    private String enableFlag;

    /**
     * 是否作为轮播图标识（0-否1-是）
     */
    @Column(name = "CAROUSEL_FLAG", columnDefinition = "char(1)")
    private String carouselFlag;

    /**
     * 创建日期
     */
    @Column(name = "CREATE_DATE", columnDefinition = "char(8)")
    private String createDate;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME", columnDefinition = "char(6)")
    private String createTime;

    /**
     * 经度
     */
    @Column(name = "MAP_LNG", length = 64, nullable = false)
    private String mapLng;

    /**
     * 纬度
     */
    @Column(name = "MAP_LAT", length = 64, nullable = false)
    private String mapLat;

    /**
     * 是否推荐标识（0-不推荐1-推荐），默认为0-不推荐
     */
    @Column(name = "RECOMMEND_FLAG", columnDefinition = "char(1)")
    private String recommendFlag;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "merchantInfo")
    private List<MerchantInfoExt> merchantInfoExt;

}
