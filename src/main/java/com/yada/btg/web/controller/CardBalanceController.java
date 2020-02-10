package com.yada.btg.web.controller;

import cfca.sadk.control.sip.DecryptionException;
import cfca.sadk.control.sip.api.SIPDecryptor;
import com.yada.btg.web.entity.MsgContext;
import com.yada.btg.web.service.CardBalanceService;
import com.yada.btg.web.service.CardBinTypeService;
import com.yada.btg.web.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

/**
 * @author wds
 * @date 2020/01/02
 * <p>
 * Description:卡余额查询处理controller
 */
@Controller
@RequestMapping("/balance")
public class CardBalanceController {

    /**
     * 异常信息
     */
    @Value("${comm.errorMsg}")
    public String errMsg;
    /**
     * 与卡系统数据交互，成功状态码
     */
    private static final String SUCCESS_RSP_CODE = "0000";
    /**
     * 跳转到错误页面
     */
    private static final String FAIL_PAGE = "redirect:/globalError";
    /**
     * 跳转到余额查询页面
     */
    private static final String BALANCE_PAGE = "balance/balancePage";
    /**
     * SIP协议解密程序
     */
    private final SIPDecryptor sipDecryptor;
    /**
     * cardBin业务层
     */
    private final CardBinTypeService cardBinTypeService;
    /**
     * 卡余额查询业务层
     */
    private final CardBalanceService cardBalanceService;

    @Autowired
    public CardBalanceController(SIPDecryptor sipDecryptor, CardBinTypeService cardBinTypeService, CardBalanceService cardBalanceService) {
        this.sipDecryptor = sipDecryptor;
        this.cardBinTypeService = cardBinTypeService;
        this.cardBalanceService = cardBalanceService;
    }

    /**
     * 卡余额查询前置
     *
     * @param model model
     * @return 页面
     */
    @RequestMapping("/list")
    public String cardRemainPre(
            @ModelAttribute(name = "msg") MsgContext msg,
            Model model, RedirectAttributes redirectAttributes) {
        try {
            LogUtil.info("接收到官网的卡余额查询请求");
            //设置服务器随机数以及结果框是否显示
            setServerRandomAndIsHidden(model, "true");
            //跳转到余额查询页面
            return BALANCE_PAGE;
        } catch (Exception e) {
            LogUtil.error("系统错误", e);
            //跳转到错误页面
            redirectAttributes.addAttribute("errMsg", errMsg);
            redirectAttributes.addAttribute("errCode", 9999);
            return FAIL_PAGE;
        }
    }

    /**
     * 余额查询操作
     *
     * @param cardNo       卡号
     * @param password     密码
     * @param clientRandom 客户端随机数
     * @param serverRandom 服务端随机数
     * @return 跳转到对应页面
     */
    @RequestMapping("/remain")
    public String remain(
            @ModelAttribute(name = "msg") MsgContext msg,
            String cardNo,
            String password,
            Model model,
            String clientRandom,
            String serverRandom,
            RedirectAttributes redirectAttributes) {
        try {
            //对卡号进行数据处理
            String cardNoNew = cardNo.replaceAll(" ", "");
            LogUtil.info("开始进行非实名卡余额查询操作，页面传过来的数据卡号：" + cardNoNew + ",密码：" + password);
            //对密码进行数据处理
            String encryptedPwd = sipDecryptor.decrypt(serverRandom, clientRandom, password);
            //判断卡号与密码是否符合查询条件
            if (cardBalanceService.verifyData(cardNoNew, encryptedPwd, model)) {
                //设置服务器随机数以及结果框是否显示
                setServerRandomAndIsHidden(model, "true");
                return BALANCE_PAGE;
            }
            //通过卡号与密码进行查询余额操作，并接收查询结果(一个带有数据的map)
            Map resultMap = cardBalanceService.remain(cardNoNew, encryptedPwd);
            //判断返回数据中的状态码是否不为“0000”
            String rspCode = resultMap.get("RspCode").toString();
            if (!SUCCESS_RSP_CODE.equalsIgnoreCase(rspCode)) {
                model.addAttribute("errorMsg", resultMap.get("RspMsg"));
                //设置服务器随机数以及结果框是否显示
                setServerRandomAndIsHidden(model, "true");
                return BALANCE_PAGE;
            }
            //将查询到的结果共享到前端页面
            model.addAttribute("cardNo", resultMap.get("CardNo").toString());
            model.addAttribute("bal", resultMap.get("Bal").toString());
            model.addAttribute("expDate", resultMap.get("ExpDate").toString());
            model.addAttribute("status", resultMap.get("Status").toString());
            model.addAttribute("attribute", resultMap.get("Attribute").toString());
            //设置服务器随机数以及结果框是否显示
            setServerRandomAndIsHidden(model, "false");
            //跳转到余额查询页面
            return BALANCE_PAGE;
        } catch (Exception e) {
            LogUtil.error("系统出错", e);
            //跳转到错误页面
            redirectAttributes.addAttribute("errMsg", errMsg);
            redirectAttributes.addAttribute("errCode", 9999);
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
    @RequestMapping("ajax_CheckCardNo")
    public String ajaxCheckCardNo(String cardNo) {
        return cardBinTypeService.verifyCard(cardNo);
    }

    /**
     * 共享服务器随机数，以及结果框是否显示的私有方法
     *
     * @param model 用于共享数据
     * @param flag  结果框是否显示
     * @throws DecryptionException 生成服务器随机码异常
     */
    private void setServerRandomAndIsHidden(Model model, String flag) throws DecryptionException {
        //共享服务端随机数
        model.addAttribute("serverRandom", sipDecryptor.generateServerRandom());
        //设置显示余额的效果为不显示
        model.addAttribute("isHidden", flag);
    }
}