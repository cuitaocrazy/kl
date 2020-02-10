package com.yada.btg.web.controller;

import com.yada.btg.web.service.CardBinTypeService;
import com.yada.btg.web.service.CardLostService;
import com.yada.btg.web.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;

/**
 * @author zsy
 * @date 2019/12/18
 * Description:实名卡挂失处理controller
 */
@Controller
@RequestMapping("lost")
public class CardLostController {

    /**
     * 卡号不存在
     */
    @Value("${comm.cardNoNotExist}")
    private String cardNoNotExist;

    /**
     * 证件号输入有误，请重新输入
     */
    @Value("${lost.idNoErrorMsg}")
    private String idNoErrorMsg;

    /**
     * 备注不能为空且不可超长,请重新输入备注
     */
    @Value("${comm.remarksVerifyError}")
    private String remarksVerifyError;

    /**
     * 备注最大长度
     */
    private static final int MAX_LENGTH = 512;
    private static final String CODE = "GBK";

    /**
     * 挂失页面路径
     */
    private static final String CARD_LOST = "lost/lostPage";

    private final CardLostService cardLostService;
    private final CardBinTypeService cardBinTypeService;

    @Autowired
    public CardLostController(CardLostService cardLostService,
                              CardBinTypeService cardBinTypeService) {
        this.cardLostService = cardLostService;
        this.cardBinTypeService = cardBinTypeService;
    }

    /**
     * 跳转挂失页面
     *
     * @return 页面路径
     */
    @RequestMapping("/list")
    public String lost() {
        return CARD_LOST;
    }

    /**
     * ajax执行挂失操作
     *
     * @param cardNo  卡号
     * @param idNo    证件号
     * @param remarks 备注
     * @return 挂失结果信息
     */
    @RequestMapping("/ajax_report")
    @ResponseBody
    public String report(String cardNo, String idNo, String remarks) throws UnsupportedEncodingException {
        LogUtil.info("开始执行卡挂失操作，传入卡号为：" + cardNo + "，传入证件号为：" + idNo + "，传入备注为：" + remarks);
        String cardNoNew = cardNo.replaceAll(" ", "");
        String idNoNew = idNo.replaceAll(" ", "");
        //开始校验卡号，证件号，备注
        String cardNoRule = "^[0-9]{7,19}$";
        String idNoNewRule = "^[0-9xX]{1,30}$";
        if (!cardNoNew.matches(cardNoRule)) {
            return cardNoNotExist;
        } else if (!idNoNew.matches(idNoNewRule)) {
            return idNoErrorMsg;
        } else if (StringUtils.isEmpty(remarks) || remarks.getBytes(CODE).length > MAX_LENGTH) {
            return remarksVerifyError;
        }
        return cardLostService.report(cardNoNew, idNoNew, remarks);
    }

    /**
     * ajax校验卡bin
     *
     * @param cardNo 卡号
     * @return 校验结果，成功返回""
     */
    @RequestMapping("/ajax_CheckCardNo")
    @ResponseBody
    public String ajaxCheckCardNo(String cardNo) {
        return cardBinTypeService.verifyCard(cardNo);
    }
}
