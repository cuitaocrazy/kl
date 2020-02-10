package com.yada.btg.web.entity;

import lombok.Data;

/**
 * @author wh
 * @date 2019/12/31
 * Description:消费记录实体
 */
@SuppressWarnings("unused")
@Data
public class TranRecode {
    /**
     * 交易日期
     */
    private String trDate;
    /**
     * 交易时间
     */
    private String trTime;

    private String trCode;
    /**
     * 交易记录名
     */
    private String trName;
    /**
     * 卡号
     */
    private String cardNo;

    private String sysTrId;

    private String deviceTrId;
    /**
     * 终端编号
     */
    private String termId;
    /**
     * 商户编号
     */
    private String merId;
    /**
     * 商户名
     */
    private String merName;
    /**
     * 交易金额
     */
    private String trAmt;
    /**
     * 余额
     */
    private String bal;
    /**
     *  日期+时间
     */
    private String trDateTime;

    public String getTrDateTime() {
        if (trDate == null || trTime == null || trDate.length() < 8 || trTime.length() < 6) {
            return "";
        }
        return this.trDate.substring(0, 4) + "/" + this.trDate.substring(4, 6) + "/" + this.trDate.substring(6, 8) + "  " + this.trTime.substring(0, 2) + ":" + this.getTrTime().substring(2, 4) + ":" + this.getTrTime().substring(4, 6);
    }
}
