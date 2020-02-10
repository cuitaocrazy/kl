package com.yada.btg.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:商户主信息管理查询条件
 *
 * @author songpeijiang
 * @since 2020/1/8
 */
@Data
@AllArgsConstructor
public class MerchantInfoQuery implements Specification<MerchantInfo> {

    private static final long serialVersionUID = 3168674818577411094L;
    /**
     * 商户标题
     */
    private String merTitle;
    /**
     * 特惠商户标识
     */
    private String discountMerFlag;
    /**
     * 所属区域
     */
    private String area;
    /**
     * 商户类型
     */
    private String merType;
    /**
     * 卡类型
     */
    private String[] cardType;
    /**
     * 启用标识
     */
    private String enableFlag;
    /**
     * 轮播标识
     */
    private String carouselFlag;

    @Override
    public Predicate toPredicate(Root<MerchantInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> conditionList = new ArrayList<>();
        if (!StringUtils.isEmpty(merTitle) && (!"全部".equals(merTitle))) {
            conditionList.add(criteriaBuilder.like(root.get("merTitle").as(String.class), "%" + merTitle + "%"));
        }
        if (!StringUtils.isEmpty(discountMerFlag) && (!"全部".equals(discountMerFlag))) {
            if ("有优惠".equals(discountMerFlag)) {
                conditionList.add(criteriaBuilder.equal(root.get("discountMerFlag").as(String.class), "1"));
            } else {
                conditionList.add(criteriaBuilder.equal(root.get("discountMerFlag").as(String.class), "0"));
            }
        }
        if (!StringUtils.isEmpty(area) && (!"全部".equals(area))) {
            conditionList.add(criteriaBuilder.equal(root.get("area").as(String.class), area));
        }
        if (!StringUtils.isEmpty(merType) && (!"全部".equals(merType))) {
            conditionList.add(criteriaBuilder.equal(root.get("merType").as(String.class), merType));
        }
        if (cardType != null && cardType.length != 0) {
            List<Predicate> cardTypeList = new ArrayList<>();
            Predicate[] cardTypes = new Predicate[cardType.length];
            for (String ct : cardType) {
                cardTypeList.add(criteriaBuilder.like(root.get("cardType").as(String.class), "%" + ct + "%"));
            }
            cardTypeList.toArray(cardTypes);
            conditionList.add(criteriaBuilder.or(cardTypes));
        }
        if (!StringUtils.isEmpty(enableFlag)) {
            conditionList.add(criteriaBuilder.equal(root.get("enableFlag").as(String.class), enableFlag));
        }
        if (!StringUtils.isEmpty(carouselFlag)) {
            conditionList.add(criteriaBuilder.equal(root.get("carouselFlag").as(String.class), carouselFlag));
        }
        if (conditionList.size() > 0) {
            criteriaQuery.where(conditionList.toArray(new Predicate[0]));
        }
        return criteriaQuery.getRestriction();
    }

}
