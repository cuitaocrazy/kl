package com.yada.btg.web.controller;

import cfca.sadk.control.sip.api.SIPDecryptor;
import com.yada.btg.web.service.CardBinTypeService;
import com.yada.btg.web.service.CardDelayService;
import com.yada.btg.web.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zsy
 * @date 2019/12/18
 * <p>
 * 延期操作处理controller
 */
@Controller
@RequestMapping("/delay")
public class CardDelayController {
    /**
     * 非实名卡延期异常
     */
    @Value("${comm.errorMsg}")
    public String errorMsg;

    //失败页面
    private static final String FAIL_PAGE = "globalError";
    private static final String CARD_DELAY = "delay/delayPage";
    private final CardDelayService cardDelayService;
    private final SIPDecryptor sipDecryptor;
    private final CardBinTypeService cardBinTypeService;

    @Autowired
    public CardDelayController(CardDelayService cardDelayService,
                               SIPDecryptor sipDecryptor,
                               CardBinTypeService cardBinTypeService) {
        this.cardDelayService = cardDelayService;
        this.sipDecryptor = sipDecryptor;
        this.cardBinTypeService = cardBinTypeService;
    }

    @RequestMapping("/list")
    public String delay(Model model) {
        try {
            LogUtil.info("接收到官网的延期请求");
            model.addAttribute("serverRandom", sipDecryptor.generateServerRandom());
        } catch (Exception e) {
            LogUtil.error("系统异常", e);
            return FAIL_PAGE;
        }
        return CARD_DELAY;
    }

    /**
     * 延期操作
     *
     * @param cardNo       卡号
     * @param password     密码
     * @param remarks      备注
     * @param clientRandom 客户端随机数
     * @param serverRandom 服务端随机数
     */
    @ResponseBody
    @RequestMapping("/ajax_QueryDelayInfo")
    public String delayInfo(String cardNo,
                            String password,
                            String remarks,
                            String clientRandom,
                            String serverRandom) {
        try {
            String cardNoNew = cardNo.replaceAll(" ", "");
            LogUtil.info("开始进行非实名卡延期操作，页面传过来的数据卡号：" + cardNo + ",密码：" + password + ",备注：" + remarks);
            String encryptedPwd = sipDecryptor.decrypt(serverRandom, clientRandom, password);
            if (cardDelayService.verifyData(cardNoNew, encryptedPwd, remarks)) {
                return FAIL_PAGE;
            }
            String result = cardDelayService.cardDelay(cardNoNew, encryptedPwd, remarks);
            if (!errorMsg.equals(result)) {
                return result;
            }
            return FAIL_PAGE;
        } catch (Exception e) {
            LogUtil.error("系统异常", e);
            return FAIL_PAGE;
        }
    }

    /**
     * ajax校验卡bin
     *
     * @param cardNo 卡号
     * @return 校验结果 返回空窜标识成功
     */
    @ResponseBody
    @RequestMapping("/ajax_CheckCardNo")
    public String ajax_CheckCardNo(String cardNo) {
        return cardBinTypeService.verifyCard(cardNo);
    }
}