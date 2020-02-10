package com.yada.btg.web.service;

import com.yada.btg.web.entity.DictInfo;
import com.yada.btg.web.entity.MerchantInfo;
import com.yada.btg.web.entity.MerchantInfoQuery;
import com.yada.btg.web.repository.DictItemRepository;
import com.yada.btg.web.repository.MerchantInfoRepository;
import com.yada.btg.web.util.CommUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:特约商户信息查询
 *
 * @author songpeijiang
 * @since 2020/1/6
 */
@Service
public class MerchantInfoService {

    /**
     * 地图url的前缀
     */
    @Value("${map.noMapPre}")
    public String noMapPre;
    /**
     * 地图url的type
     */
    @Value("${map.noType}")
    public String noType;
    /**
     * 地图url的isopeninfowin
     */
    @Value("${map.noIsopeninfowin}")
    public String noIsopeninfowin;
    /**
     * 地图url的markertype
     */
    @Value("${map.noMarkertype}")
    public String noMarkertype;
    /**
     * 地图url的ref
     */
    @Value("${map.ref}")
    public String ref;

    private final MerchantInfoRepository merchantInfoRepository;
    private final DictItemRepository dictItemRepository;

    @Autowired
    public MerchantInfoService(MerchantInfoRepository merchantInfoRepository, DictItemRepository dictItemRepository) {
        this.merchantInfoRepository = merchantInfoRepository;
        this.dictItemRepository = dictItemRepository;
    }

    /**
     * 查询所有的查询条件
     *
     * @return 数字字典集合
     */
    @Cacheable(cacheNames = "merQueryCache")
    public Map<String, Object> merQueryMain() {
        Map<String, Object> modelMap = new HashMap<>();
        List<DictInfo> dictItemList = dictItemRepository.findAll();
        List<DictInfo> areaList = new ArrayList<>();
        List<DictInfo> merTypeList = new ArrayList<>();
        List<DictInfo> cardTypeList = new ArrayList<>();
        List<DictInfo> discountMerFlagList = new ArrayList<>();
        for (DictInfo o : dictItemList) {
            switch (o.getItemType()) {
                case "区域":
                    areaList.add(o);
                    break;
                case "商户类型":
                    merTypeList.add(o);
                    break;
                case "卡类型":
                    cardTypeList.add(o);
                    break;
                case "优惠":
                    discountMerFlagList.add(o);
                    break;
                default:
                    break;
            }
        }
        modelMap.put("areaList", CommUtil.sortInfo(areaList));
        modelMap.put("merTypeList", CommUtil.sortInfo(merTypeList));
        modelMap.put("cardTypeList", CommUtil.sortInfo(cardTypeList));
        modelMap.put("discountMerFlagList", CommUtil.sortInfo(discountMerFlagList));
        return modelMap;
    }

    /**
     * 用于分页查询
     *
     * @param pageable        查询时插入的分页条件实体
     * @param area            地区
     * @param merType         商户类型
     * @param cardType        卡类型
     * @param discountMerFlag 是否是优惠商户
     * @param merTitle        商户标题
     * @return 分页信息
     */
    public Page<MerchantInfo> query(Pageable pageable, String area, String merType, String[] cardType, String discountMerFlag, String merTitle) {
        //封装查询条件实体
        MerchantInfoQuery merchantInfoQuery = new MerchantInfoQuery(merTitle, discountMerFlag, area, merType, cardType, "1", null);
        //分页查询
        return merchantInfoRepository.findAll(merchantInfoQuery, pageable);
    }

    /**
     * 根据商户id主键查询商户信息
     *
     * @param merInfoId 商户信息
     * @return 商户信息
     */
    public MerchantInfo queryByMerInfoId(String merInfoId) {
        return merchantInfoRepository.getOne(merInfoId);
    }

    /**
     * 查询商户地图信息
     *
     * @param merchantInfo 商户信息
     * @return 商户地图信息
     */
    public String queryMapInfo(MerchantInfo merchantInfo) {
        return noMapPre + "type=" + noType + "&isopeninfowin=" + noIsopeninfowin + "&markertype=" + noMarkertype + "&pointx=" + merchantInfo.getMapLng() + "&pointy=" + merchantInfo.getMapLat() + "&name=" + merchantInfo.getMerTitle() + "&addr=" + merchantInfo.getAddress() + "&ref=" + ref;
    }

    /**
     * 用于轮播图查询
     *
     * @param area            地区
     * @param merType         商户类型
     * @param cardType        卡类型
     * @param discountMerFlag 是否是优惠商户
     * @param merTitle        商户标题
     * @return 轮播商户信息
     */
    public List<MerchantInfo> queryMerSideShow(String area, String merType, String[] cardType, String discountMerFlag, String merTitle) {
        //封装查询条件实体
        MerchantInfoQuery merchantInfoQuery = new MerchantInfoQuery(merTitle, discountMerFlag, area, merType, cardType, "1", "1");
        //分页查询
        return merchantInfoRepository.findAll(merchantInfoQuery);
    }
}
