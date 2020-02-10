package com.yada.btg.web.entity;

import lombok.Data;

import javax.persistence.Lob;
import java.util.List;

/**
 * @author yn on 2019/12/20.
 * 加载公共数据的类实体
 */
@Data
public class MsgContext {
    /**
     * 首页菜单Id
     */
    private String homeMenuId;

    /**
     * 业务介绍菜单Id
     */
    private String businessMenuId;

    /**
     * 新闻资讯菜单Id
     */
    private String newsMenuId;

    /**
     * 磁盘图片展示需要添加的前缀（ip）
     */
    private String imgPre;

    /**
     * 不需要获取二级菜单信息的路径
     */
    String menuPath;

    /**
     * 一级菜单列表
     */
    private List<MenuOne> menuOneList;

    /**
     * 二级菜单列表
     */
    private List<MenuTwo> menuTwoList;

    /**
     * 菜单图片列表
     */
    private List<MenuImgExt> menuImgList;

    /**
     * 展示在头部的菜单
     */
    private List<MenuOne> headMenuList;

    /**
     * 公共底部大字段
     */
    @Lob
    private String footRes;

    /**
     * 公共底部商户链接列表
     */
    private List<MerLink> merLinkList;

    /**
     * 首页业务介绍(4条)
     */
    private List<BusinessInfo> recommendBusinessList;

    /**
     * 首页推荐商户列表（8条）
     */
    private List<MerchantInfo> recommendMerList;
}


