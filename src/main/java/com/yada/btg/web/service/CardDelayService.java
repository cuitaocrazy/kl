package com.yada.btg.web.service;

import com.yada.btg.web.entity.CardTypeEnum;
import com.yada.btg.web.entity.RouteInfo;
import com.yada.btg.web.sys.RestHttpClient;
import com.yada.btg.web.util.GenCardTypeUtil;
import com.yada.btg.web.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lj on 2019/12/23
 * <p>
 * Description:延期操作逻辑处理
 *
 * @author lj
 */
@Service
public class CardDelayService {

    /**
     * 非实名卡延期异常
     */
    @Value("${comm.errorMsg}")
    public String errorMsg;

    /**
     * 非实名卡延期密码不正确
     */
    @Value("${comm.pwdVerifyError}")
    public String pwdVerifyError;

    /**
     * 非实名延期备注不正确
     */
    @Value("${comm.remarksVerifyError}")
    public String remarksVerifyError;

    /**
     * 非实名卡延期卡号不存在
     */
    @Value("${comm.cardNoNotExist}")
    public String cardNoNotExist;

    /**
     * 非实名卡延期成功提示前半部分
     */
    @Value("${delay.successMsg}")
    public String successMsg;

    /**
     * 与卡系统数据交互，成功状态码
     */
    private static final String SUCCESS_RSP_CODE = "0000";

    private final GenCardTypeUtil genCardTypeUtil;
    private final RestHttpClient restHttpClient;

    @Autowired
    public CardDelayService(GenCardTypeUtil genCardTypeUtil, RestHttpClient restHttpClient) {
        this.genCardTypeUtil = genCardTypeUtil;
        this.restHttpClient = restHttpClient;
    }

    /**
     * 卡延期操作
     *
     * @param cardNo   卡号
     * @param password 密码
     * @param remarks  备注
     */
    public String cardDelay(String cardNo, String password, String remarks) {
        try {
            CardTypeEnum cardTypeEnum = genCardTypeUtil.getCardType(cardNo.substring(0, 7));
            Map<String, Object> bodyMap = new LinkedHashMap<>();
            bodyMap.put("OperateType", "2");
            bodyMap.put("CardNo", cardNo);
            bodyMap.put("Password", password);
            bodyMap.put("Comments", remarks);
            Map<String, Object> delayMap = restHttpClient.getSendMapData("5017", "REQ5017", cardTypeEnum, RouteInfo.DELAY, bodyMap);
            Map<String, Object> resultMap = restHttpClient.send(delayMap);
            Map<String, String> respMap = (Map<String, String>) resultMap.get("RespData");
            String respCode = respMap.get("RspCode");
            String addData = respMap.get("AddData");
            LogUtil.info("获取到卡系统返回的数据，响应码：" + respCode + ",新的有效期：" + addData);
            if ((SUCCESS_RSP_CODE.equals(respCode)) && (addData != null && !"".equals(addData))) {
                return "true-" + successMsg;
            } else if (SUCCESS_RSP_CODE.equals(respCode)) {
                return "true-" + successMsg;
            } else {
                return "false-" + respMap.get("RspMsg");
            }
        } catch (Exception e) {
            LogUtil.error("系统异常", e);
            return errorMsg;
        }
    }

    /**
     * 校验数据
     *
     * @param cardNo   卡号
     * @param password 密码
     * @param remarks  备注
     */
    public boolean verifyData(String cardNo, String password, String remarks) throws UnsupportedEncodingException {
        String cardNoRule = "^[0-9]{7,19}$";
        String pwdRule = "^[0-9]{6}";
        String errorMsg;
        int remarksLen = remarks.getBytes("GBK").length;
        if (!cardNo.matches(cardNoRule)) {
            errorMsg = cardNoNotExist;
        } else if (!password.matches(pwdRule)) {
            errorMsg = pwdVerifyError;
        } else if (!((remarksLen >= 1) && (remarksLen <= 512))) {
            errorMsg = remarksVerifyError;
        } else {
            return false;
        }
        if (!"".equals(errorMsg)) {
            LogUtil.info("校验数据发生错误，原因是:" + errorMsg);
        }
        return true;
    }
}