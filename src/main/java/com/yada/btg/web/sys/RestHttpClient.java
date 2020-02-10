package com.yada.btg.web.sys;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yada.btg.web.entity.CardTypeEnum;
import com.yada.btg.web.entity.RouteInfo;
import com.yada.btg.web.entity.TranRecode;
import com.yada.btg.web.util.CommUtil;
import com.yada.btg.web.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author zsy
 * @date 2019/12/20
 * 连接与卡系统交互模块的客户端
 */
@Service
public class RestHttpClient {
    /**
     * 发送数据到的地址
     */
    @Value("${connCard.sendDataToOut}")
    String sendDataToOut;
    /**
     * 响应给用户看的错误响应信息
     */
    @Value("${comm.errorMsg}")
    String errorMsg;
    /**
     * 和与卡系统交互模块对接正常系统返回码
     */
    private static final String SUCCESS_CODE = "0000";
    /**
     * 和与卡系统交互模块对接异常系统返回码
     */
    private static final String FAIL_CODE = "9999";
    /**
     * 当交易不成功时返回的响应码
     */
    private static final String FAIL_CODE_RSP = "-9999";

    private final RestTemplate restTemplate;

    @Autowired
    public RestHttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 获取发送的Map数据
     *
     * @param cardTypeEnum 枚举值
     * @param route        路由枚举类
     * @param bodyMap      报文体Map
     */
    public Map<String, Object> getSendMapData(String msgNo, String msgNoWithReq, CardTypeEnum cardTypeEnum, RouteInfo route, Map<String, Object> bodyMap) {
        String msgId = CommUtil.getMsgId();
        Map<String, Object> delayMap = new LinkedHashMap<>();
        delayMap.put("MsgNo", msgNo);
        delayMap.put("MsgNoWithReq", msgNoWithReq);
        delayMap.put("MsgId", msgId);
        delayMap.put("CardTypeEnum", cardTypeEnum);
        delayMap.put("Route", route);
        delayMap.put("BodyData", bodyMap);
        return delayMap;

    }

    /**
     * 发送请求数据到与卡系统交互模块
     *
     * @param map 请求数据
     * @return 返回数据
     */
    public Map<String, Object> send(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>();
        try {
            ResponseEntity<String> forObject = restTemplate.postForEntity(sendDataToOut, map, String.class);
            ObjectMapper mapper = new ObjectMapper();
            Map resultMap = mapper.readValue(Objects.requireNonNull(forObject.getBody()), Map.class);
            if (SUCCESS_CODE.equals(resultMap.get("SysCode").toString()) && RouteInfo.CONSUME.equals(RouteInfo.valueOf(map.get("Route").toString()))) {
                JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, TranRecode.class);
                String respData = JSON.toJSONString(resultMap.get("RespData"));
                result.put("RespData", mapper.readValue(respData, javaType));
            } else {
                JavaType javaType = mapper.getTypeFactory().constructParametricType(Map.class, String.class, String.class);
                String respData = JSON.toJSONString(resultMap.get("RespData"));
                result.put("RespData", mapper.readValue(respData, javaType));
            }
            result.put("SysCode", resultMap.get("SysCode"));
            return result;
        } catch (Throwable throwable) {
            LogUtil.error("与连接卡系统程序交互出现异常", throwable);
            result.put("SysCode", FAIL_CODE);
            Map<String, String> respData = new HashMap<>();
            respData.put("RspCode", FAIL_CODE_RSP);
            respData.put("RspMsg", errorMsg);
            result.put("RespData", respData);
            return result;
        }

    }
}
