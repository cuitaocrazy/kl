package com.yada.btg.web.service;

import com.yada.btg.web.entity.CardBinType;
import com.yada.btg.web.entity.CardTypeEnum;
import com.yada.btg.web.entity.RouteInfo;
import com.yada.btg.web.repository.CardBinTypeRepository;
import com.yada.btg.web.sys.RestHttpClient;
import com.yada.btg.web.util.CommUtil;
import com.yada.btg.web.util.GenCardTypeUtil;
import com.yada.btg.web.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author wds
 * @date 2020/01/02
 * Description:卡余额查询操作逻辑处理
 */
@Service
public class CardBalanceService {
    /**
     * 余额查询异常
     */
    @Value("${comm.errorMsg}")
    public String errorMsg;
    /**
     * 余额查询密码格式不正确
     */
    @Value("${comm.pwdVerifyError}")
    public String pwdVerifyError;

    /**
     * 余额查询卡号不存在
     */
    @Value("${comm.cardNoNotExist}")
    public String cardNoNotExist;
    /**
     * 卡bin的repository类
     */
    private CardBinTypeRepository cardBinTypeRepository;
    /**
     * 卡交互客户端
     */
    private RestHttpClient restHttpClient;
    /**
     * 卡类型枚举map
     */
    private GenCardTypeUtil genCardTypeUtil;

    @Autowired
    public CardBalanceService(CardBinTypeRepository cardBinTypeRepository, RestHttpClient restHttpClient, GenCardTypeUtil genCardTypeUtil) {
        this.cardBinTypeRepository = cardBinTypeRepository;
        this.restHttpClient = restHttpClient;
        this.genCardTypeUtil = genCardTypeUtil;
    }

    /**
     * 获取余额信息
     *
     * @param cardNo   卡号
     * @param password 密码
     * @return 页面
     */
    public Map remain(String cardNo, String password) {
        //结果map
        Map<String, String> respMap = new HashMap<>(0);
        try {
            //将卡数据放进bodyDate中
            Map<String, Object> bodyData = new HashMap<>(2);
            bodyData.put("Password", password);
            bodyData.put("CardNo", cardNo);
            //根据卡bin获取卡类型
            CardTypeEnum cardTypeEnum = genCardTypeUtil.getCardType(cardNo.substring(0, 7));
            //获取与卡交互数据balanceMap
            Map<String, Object> balanceMap = restHttpClient.getSendMapData("6101","REQ6101",cardTypeEnum,RouteInfo.BALANCE,bodyData);
            //与卡系统进行交互
            Map<String, Object> resultMap = restHttpClient.send(balanceMap);
            respMap = (Map<String, String>) resultMap.get("RespData");
        } catch (Throwable throwable) {
            LogUtil.error("数据解析异常", throwable);
        }
        return respMap;
    }

    /**
     * 校验页面传过来的数据
     *
     * @param cardNo   卡号
     * @param password 解密后的密码
     * @return 是否校验成功
     */
    public boolean verifyData(String cardNo, String password, Model model) {
        String cardNoRule = "^[0-9]{7,19}$";
        String pwdRule = "^[0-9]{6}$";
        String errorMsg;
        if (!cardNo.matches(cardNoRule)) {
            errorMsg = cardNoNotExist;
        } else if (!password.matches(pwdRule)) {
            errorMsg = pwdVerifyError;
        } else {
            String cardBin = cardNo.substring(0, 7);
            List<CardBinType> cardBinTypeList = cardBinTypeRepository.findByCardBinStartingWith(cardBin);
            assert cardBinTypeList != null;
            if (cardBinTypeList.size() > 0) {
                return false;
            }
            errorMsg = cardNoNotExist;
        }
        LogUtil.info("校验数据发生错误，原因是:[{}]" + errorMsg);
        model.addAttribute("errorMsg", errorMsg);
        return true;
    }
}
