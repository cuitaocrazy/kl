package com.yada.btg.web.service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yada.btg.web.entity.CardTypeEnum;
import com.yada.btg.web.entity.RouteInfo;
import com.yada.btg.web.entity.TranRecode;
import com.yada.btg.web.sys.RestHttpClient;
import com.yada.btg.web.util.GenCardTypeUtil;
import com.yada.btg.web.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

/**
 * @author wh
 * @date 2019/12/31
 */
@Service
public class CardConsumeService {

    /**
     * 系统错误
     */
    @Value("${comm.errorMsg}")
    public String errorMsg;

    /**
     * 卡号不存在
     */
    @Value("${comm.cardNoNotExist}")
    public String cardNoNotExist;

    /**
     * 密码不正确
     */
    @Value("${comm.pwdVerifyError}")
    public String pwdVerifyError;

    /**
     * 时间不正确
     */
    @Value("${info.timeFormatErrorMsg}")
    public String timeFormatErrorMsg;

    private RestHttpClient restHttpClient;
    /**
     * 获取卡类型工具类
     */
    private final GenCardTypeUtil genCardTypeUtil;

    @Autowired
    public CardConsumeService(RestHttpClient restHttpClient, GenCardTypeUtil genCardTypeUtil) {
        this.restHttpClient = restHttpClient;
        this.genCardTypeUtil = genCardTypeUtil;
    }

    /**
     * 校验页面传过来的数据
     *
     * @param newCardNo    卡号
     * @param encryptedPwd 解密后的密码
     * @param startDate    开始时间
     * @param endDate      结束时间
     * @return 是否校验成功
     */
    public boolean verifyData(String newCardNo, String encryptedPwd, String startDate, String endDate, Model model) {
        String cardNoRule = "^[0-9]{7,19}$";
        String pwdRule = "^[0-9]{6}$";
        String errorMsgLog;
        //校验卡号
        if (!newCardNo.matches(cardNoRule)) {
            errorMsgLog = cardNoNotExist;
            LogUtil.info("校验数据发生错误，原因是:" + errorMsgLog + "");
            model.addAttribute("RspMsg", errorMsgLog);
            return false;
        }
        //校验密码
        if (!encryptedPwd.matches(pwdRule)) {
            errorMsgLog = pwdVerifyError;
            model.addAttribute("RspMsg", errorMsgLog);
            LogUtil.info("校验数据发生错误，原因是:" + errorMsgLog + "");
            return false;
        }
        //校验开始时间，结束时间
        if (startDate == null) {
            startDate = "";
        }
        if (endDate == null) {
            endDate = "";
        }
        startDate = startDate.replaceAll("-", "");
        endDate = endDate.replaceAll("-", "");
        //定义日期或时间的长度
        int num = 8;
        if (startDate.length() != num) {
            errorMsgLog = timeFormatErrorMsg;
            model.addAttribute("RspMsg", errorMsgLog);
            LogUtil.info("校验数据发生错误，原因是:" + errorMsgLog + "");
            return false;
        }
        if (endDate.length() != num) {
            errorMsgLog = timeFormatErrorMsg;
            LogUtil.info("校验数据发生错误，原因是:" + errorMsgLog + "");
            model.addAttribute("RspMsg", errorMsgLog);
            return false;
        }
        return true;
    }

    /**
     * 消费记录查询
     *
     * @param newCardNo    卡号
     * @param encryptedPwd 解密后密码
     * @param startDate    开始时间
     * @param endDate      结束时间
     * @return 消费记录查询结果实体集合
     */
    public Map<String, Object> consumptionRecord(String newCardNo, String encryptedPwd, String startDate, String endDate) {
        LogUtil.info("卡消费记录页面传过来的数据校验成功,组装请求数据CardNo:" + newCardNo + "");
        //卡类型
        CardTypeEnum cardTypeEnum = genCardTypeUtil.getCardType(newCardNo.substring(0, 7));
        //组装请求数据
        Map<String, Object> bodyMap = new HashMap<>(16);
        bodyMap.put("CardNo", newCardNo);
        bodyMap.put("Password", encryptedPwd);
        bodyMap.put("StartDate", startDate);
        bodyMap.put("EndDate", endDate);
        //卡交互系统数据
        Map<String, Object> consumeMap = restHttpClient.getSendMapData("6102", "REQ6102", cardTypeEnum, RouteInfo.CONSUME, bodyMap);
        //卡消费记录查询，返回Map
        return restHttpClient.send(consumeMap);
    }

    /**
     * object转化为list
     *
     * @param obj object
     * @return List<TranRecode>
     */
    public List<TranRecode> objToList(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, TranRecode.class);
        String respData = JSON.toJSONString(obj);
        return mapper.readValue(respData, javaType);
    }

    /**
     * object转化为map
     *
     * @param obj object
     * @return Map
     */
    public Map<String, String> objToMap(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(Map.class, String.class, String.class);
        String respData = JSON.toJSONString(obj);
        return mapper.readValue(respData, javaType);
    }
}
