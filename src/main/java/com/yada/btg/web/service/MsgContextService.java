package com.yada.btg.web.service;

import com.yada.btg.web.entity.MenuOne;
import com.yada.btg.web.entity.MsgContext;
import com.yada.btg.web.entity.StaticRes;
import com.yada.btg.web.repository.*;
import com.yada.btg.web.util.CommUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yn on 2019/12/27.
 * 公共数据绑定Service
 * 基础数据：一级菜单、二级菜单、菜单图片
 * 公共数据：公共头部菜单列表、公共尾部商户链接
 * 首页数据：首页的推荐业务、首页的栏目类型
 */
@Service
public class MsgContextService {

    /**
     * 首页菜单编码
     */
    @Value("${param.menu.homeId}")
    String homeMenuId;

    /**
     * 业务介绍菜单编码
     */
    @Value("${param.menu.businessId}")
    String businessMenuId;

    /**
     * 新闻资讯菜单编码
     */
    @Value("${param.menu.newsId}")
    String newsMenuId;

    /**
     * 磁盘图片展示需要添加的前缀（ip）
     */
    @Value("${img.imgPre}")
    String imgPre;

    /**
     * 底部静态资源名称
     */
    @Value("${param.staticRes.footName}")
    String footName;

    /**
     * 不需要获取二级菜单信息的路径
     */
    @Value("${param.menu.paths}")
    String menuPath;

    private MenuOneRepository menuOneRepository;
    private MenuTwoRepository menuTwoRepository;
    private MenuImgExtRepository menuImgRepository;
    private BusinessInfoRepository businessInfoRepository;
    private MerchantInfoRepository merchantInfoRepository;
    private StaticResRepository staticResRepository;
    private MerLinkRepository merLinkRepository;

    public MsgContextService(MenuOneRepository menuOneRepository, MenuTwoRepository menuTwoRepository, MenuImgExtRepository menuImgRepository, BusinessInfoRepository businessInfoRepository, MerchantInfoRepository merchantInfoRepository, StaticResRepository staticResRepository, MerLinkRepository merLinkRepository) {
        this.menuOneRepository = menuOneRepository;
        this.menuTwoRepository = menuTwoRepository;
        this.menuImgRepository = menuImgRepository;
        this.businessInfoRepository = businessInfoRepository;
        this.merchantInfoRepository = merchantInfoRepository;
        this.staticResRepository = staticResRepository;
        this.merLinkRepository = merLinkRepository;
    }

    @Cacheable(cacheNames = "msgContextCache")
    public MsgContext getMsgContext() {
        MsgContext msg = new MsgContext();
        //获取首页菜单Id
        msg.setHomeMenuId(homeMenuId);
        //获取业务介绍菜单Id
        msg.setBusinessMenuId(businessMenuId);
        //获取新闻资讯菜单Id
        msg.setNewsMenuId(newsMenuId);
        //获取磁盘图片展示需要添加的前缀
        msg.setImgPre(imgPre);
        //不需要获取二级菜单信息的路径
        msg.setMenuPath(menuPath);
        //获取所有的一级菜单
        List<MenuOne> menuOneList = menuOneRepository.findAllByOrderByMenuWeightDesc();
        msg.setMenuOneList(menuOneList);
        //获取所有的二级菜单
        msg.setMenuTwoList(menuTwoRepository.findAllByOrderByMenuWeightDesc());
        //获取所有的菜单图片
        msg.setMenuImgList(menuImgRepository.findAll());
        //获取显示在公共头部的菜单
        msg.setHeadMenuList(CommUtil.getHeaderMenuList(menuOneList));
        //获取显示在尾部的静态数据
        StaticRes staticRes = staticResRepository.findByResName(footName);
        msg.setFootRes(staticRes != null ? staticRes.getContent() : "");
        //获取显示在公共底部的商户链接
        msg.setMerLinkList(merLinkRepository.findAllByOrderByMerOrderDesc());
        //获取显示在首页的推荐业务（管理端限制是4个）
        msg.setRecommendBusinessList(businessInfoRepository.findAllByShowFlagOrderByBusinessWeightDesc(1));
        //获取显示在首页的推荐商户
        msg.setRecommendMerList(merchantInfoRepository.findTop8ByRecommendFlagAndEnableFlag("1", "1"));

        return msg;
    }
}
