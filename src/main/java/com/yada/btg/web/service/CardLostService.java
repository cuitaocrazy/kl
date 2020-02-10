package com.yada.btg.web.service;

import com.yada.btg.web.entity.CardTypeEnum;
import com.yada.btg.web.entity.RouteInfo;
import com.yada.btg.web.sys.RestHttpClient;
import com.yada.btg.web.util.GenCardTypeUtil;
import com.yada.btg.web.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 卡挂失service
 *
 * @author cuiyongzhi
 * @date 2020/01/07
 */
@Service
public class CardLostService {

    /**
     * 已挂失
     */
    @Value("${lost.success}")
    public String success;

    private static final String SUCCESS_FLAG = "0000";
    private final GenCardTypeUtil genCardTypeUtil;
    private final RestHttpClient restHttpClient;

    @Autowired
    public CardLostService(RestHttpClient restHttpClient,
                           GenCardTypeUtil genCardTypeUtil) {
        this.restHttpClient = restHttpClient;
        this.genCardTypeUtil = genCardTypeUtil;
    }

    /**
     * 挂失操作
     *
     * @param cardNo  卡号
     * @param idNo    证件号
     * @param remarks 备注
     * @return 挂失结果
     */
    public String report(String cardNo, String idNo, String remarks) {
        LogUtil.info("开始组装请求数据");
        Map<String, Object> bodyMap = new HashMap<>(4);
        bodyMap.put("CardNo", cardNo);
        bodyMap.put("CertId", idNo);
        bodyMap.put("TrSrc", "0");
        bodyMap.put("Comments", remarks);
        CardTypeEnum cardTypeEnum = genCardTypeUtil.getCardType(cardNo.substring(0, 7));
        Map<String, Object> lostMap = restHttpClient.getSendMapData("5016", "REQ5016", cardTypeEnum, RouteInfo.LOST, bodyMap);
        LogUtil.info("请求数据组装完成，向卡系统发送请求数据：" + lostMap);
        Map<String, Object> resultMap = restHttpClient.send(lostMap);
        Map<String, String> result = (Map<String, String>) resultMap.get("RespData");
        //响应码
        String rspCode = result.get("RspCode");
        //提示信息
        String rspMsg = result.get("RspMsg");
        LogUtil.info("获取到卡系统返回的数据，响应码：" + rspCode + "，提示信息：" + rspMsg);
        if (rspCode != null && rspCode.equals(SUCCESS_FLAG)) {
            LogUtil.info("挂失操作成功：" + success);
            return success;
        } else {
            LogUtil.info("挂失操作失败:" + rspMsg);
            return rspMsg;
        }
    }
}
