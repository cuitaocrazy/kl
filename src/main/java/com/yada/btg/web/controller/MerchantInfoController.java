package com.yada.btg.web.controller;

import com.yada.btg.web.entity.MerchantInfo;
import com.yada.btg.web.entity.MsgContext;
import com.yada.btg.web.service.MerchantInfoService;
import com.yada.btg.web.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by zsy on 2019/12/18.
 * 商户查询处理controller
 *
 * @author spj
 */
@Controller
@RequestMapping("mer")
public class MerchantInfoController {

    /**
     * 磁盘图片展示需要添加的前缀（ip）
     */
    @Value("${img.imgPre}")
    String imgPre;
    /**
     * 官网请求卡类型不存在
     */
    @Value("${merweb.errorMsg}")
    String merwebErrorMsg;

    private static final String MER_DETAIL_PAGE = "mer/merchantDetails";
    private static final String MER_HOME_DETAIL_PAGE = "mer/merchantHomeDetails";
    private static final String QUERY_RESULT_PAGE = "mer/queryResult";
    private static final String FAIL_PAGE = "globalError";
    private final MerchantInfoService merchantInfoService;

    @Autowired
    public MerchantInfoController(MerchantInfoService merchantInfoService) {
        this.merchantInfoService = merchantInfoService;
    }

    /**
     * 查询所有的查询条件并展示在页面
     *
     * @param model model
     */
    @ModelAttribute
    public void preposition(Model model,
                            @ModelAttribute(name = "msg") MsgContext msgContext) {
        model.addAttribute("imgPre", imgPre);
        model.addAllAttributes(merchantInfoService.merQueryMain());
    }

    /**
     * 商户黄页的入口
     *
     * @param model           model
     * @param pageable        查询时插入的分页条件实体
     * @param area            地区
     * @param merType         商户类型
     * @param cardType        卡类型
     * @param discountMerFlag 是否是优惠商户
     * @param merTitle        商户标题
     * @return 页面
     */
    @RequestMapping("query")
    public String query(Model model,
                        @PageableDefault(value = 12) Pageable pageable,
                        String area,
                        String merType,
                        String[] cardType,
                        String discountMerFlag,
                        String merTitle) {
        if (cardType == null || cardType.length == 0) {
            model.addAttribute("errMsg", merwebErrorMsg);
            return FAIL_PAGE;
        } else {
            //分页查询
            Page<MerchantInfo> page = merchantInfoService.query(pageable, area, merType, cardType, discountMerFlag, merTitle);
            //轮播图查询
            List<MerchantInfo> sideshowMerChantList = merchantInfoService.queryMerSideShow("", "", cardType, "", "");
            model.addAttribute("page", page);
            model.addAttribute("contents", page.getContent());
            model.addAttribute("size", page.getContent().size());
            model.addAttribute("cardType", cardType);
            model.addAttribute("oldArea", area);
            model.addAttribute("oldMerType", merType);
            model.addAttribute("oldDiscountMerFlag", discountMerFlag);
            model.addAttribute("oldMerTitle", merTitle);
            model.addAttribute("sideshowMerChantList", sideshowMerChantList);
            return QUERY_RESULT_PAGE;
        }
    }

    /**
     * 根据商户id查询所有商户信息
     *
     * @param model     modelqueryByMerInfoId
     * @param merInfoId 商户Id
     * @return 页面
     */
    @RequestMapping("/queryMer")
    public String queryMer(Model model, String merInfoId) {
        LogUtil.info("根据商户id:[{" + merInfoId + "}]查询商户详情信息");
        MerchantInfo merchantInfo = merchantInfoService.queryByMerInfoId(merInfoId);
        if (merchantInfo == null) {
            LogUtil.info("查询商户信息没有结果");
            model.addAttribute("errMsg", "没有详细信息，请联系管理员");
            return FAIL_PAGE;
        } else {
            String mapInfo = merchantInfoService.queryMapInfo(merchantInfo);
            model.addAttribute("mapInfo", mapInfo);
            model.addAttribute("merchantInfo", merchantInfo);
            return MER_DETAIL_PAGE;
        }
    }

    /**
     * 根据商户id查询所有商户信息
     *
     * @param model     modelqueryByMerInfoId
     * @param merInfoId 商户Id
     * @return 页面
     */
    @RequestMapping("/merDetail")
    public String queryMerDetail(Model model, String merInfoId) {
        MerchantInfo merchantInfo = merchantInfoService.queryByMerInfoId(merInfoId);

        String mapInfo = merchantInfoService.queryMapInfo(merchantInfo);
        model.addAttribute("mapInfo", mapInfo);
        model.addAttribute("merchantInfo", merchantInfo);
        return MER_HOME_DETAIL_PAGE;
    }
}
