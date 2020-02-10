package com.yada.btg.web.controller;

import cfca.sadk.control.sip.api.SIPDecryptor;
import com.yada.btg.web.entity.MsgContext;
import com.yada.btg.web.entity.TranRecode;
import com.yada.btg.web.service.CardBinTypeService;
import com.yada.btg.web.service.CardConsumeService;
import com.yada.btg.web.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;


/**
 * @author wh
 * @date 2019/12/31
 * 卡消费记录查询处理controller
 */
@Controller
@RequestMapping("consume")
public class CardConsumeController {
    /**
     * 系统错误
     */
    @Value("${comm.errorMsg}")
    public String errorMsg;
    /**
     * 失败页面
     */
    private static final String FAIL_PAGE = "redirect:/globalError";
    /**
     * 消费记录查询信息录入页面
     */
    private static final String CONSUMPTION_RECORD = "consume/consumePage";

    private final SIPDecryptor sipDecryptor;
    private final CardConsumeService cardConsumeService;
    private final CardBinTypeService cardBinTypeService;

    @Autowired
    public CardConsumeController(CardConsumeService cardConsumeService, SIPDecryptor sipDecryptor, CardBinTypeService cardBinTypeService) {
        this.cardConsumeService = cardConsumeService;
        this.sipDecryptor = sipDecryptor;
        this.cardBinTypeService = cardBinTypeService;
    }

    /**
     * 卡消费记录查询前置
     *
     * @param model model
     * @return 页面
     */
    @RequestMapping("list")
    public String consumptionRecordPre(Model model,
                                       @ModelAttribute(name = "msg") MsgContext msg,
                                       RedirectAttributes redirectAttributes) {
        try {
            LogUtil.info("接收到官网的消费查询请求");
            //服务端随机码
            model.addAttribute("serverRandom", sipDecryptor.generateServerRandom());
            //查询结果不显示
            model.addAttribute("hasNotLs", false);
            //访问路径：consume/list
            return CONSUMPTION_RECORD;
        } catch (Exception e) {
            LogUtil.error("系统错误", e);
            redirectAttributes.addAttribute("errMsg", errorMsg);
            redirectAttributes.addAttribute("errCode", 9999);
            //返回错误页面
            return FAIL_PAGE;
        }
    }

    /**
     * 卡消费记录查询
     *
     * @param model        model
     * @param cardNo       卡号
     * @param password     密码
     * @param startDate    查询起始时间
     * @param clientRandom 客户端随机数
     * @param endDate      查询结束时间
     * @param serverRandom 服务端随机数
     * @return 页面
     */
    @RequestMapping("record")
    public String consumptionRecord(Model model,
                                    String cardNo, String password, String startDate, String endDate,
                                    String clientRandom,
                                    String serverRandom,
                                    @ModelAttribute(name = "msg") MsgContext msg,
                                    RedirectAttributes redirectAttributes) {

        try {
            //卡号去空格
            String newCardNo = cardNo.replaceAll(" ", "");
            LogUtil.info("卡消费记录，接收到卡号:" + newCardNo + ",startDate:" + startDate + ",endDate:" + endDate + "");
            //解密密码
            String encryptedPwd = sipDecryptor.decrypt(serverRandom, clientRandom, password);
            //校验卡号，密码，开始时间，结束时间
            if (!cardConsumeService.verifyData(newCardNo, encryptedPwd, startDate, endDate,model)) {
                //不符合则跳转到错误提示页面
                model.addAttribute("RspCode",9999);
                //共享服务端随机数
                model.addAttribute("serverRandom", sipDecryptor.generateServerRandom());
                //查询结果不显示
                model.addAttribute("hasNotLs", false);
                return CONSUMPTION_RECORD;
            }
            //服务端随机码
            model.addAttribute("serverRandom", sipDecryptor.generateServerRandom());
            //消费记录查询
            Map<String, Object> resultMap = cardConsumeService.consumptionRecord(newCardNo, encryptedPwd, startDate, endDate);
            // 当系统响应码为0000时，解析为list<TranRecode>,否则解析为Map
            String sysCode=resultMap.get("SysCode").toString();
            String successCode="0000";
            if (successCode.equals(sysCode)) {
                //Object转化为List<TranRecode>
                List<TranRecode> trans = cardConsumeService.objToList(resultMap.get("RespData"));
                //消费记录查询结果是否显示
                if (trans == null || trans.size() == 0) {
                    model.addAttribute("hasNotLs", true);
                } else {
                    //消费记录查询结果显示
                    model.addAttribute("hasNotLs", false);
                }
                model.addAttribute("trans", trans);
                return CONSUMPTION_RECORD;
            } else {
                //Object转化为 Map<String, String>
                Map<String, String> errorResultMap = cardConsumeService.objToMap(resultMap.get("RespData"));
                //不为“0000”说明数据交互失败，返回错误信息
                model.addAttribute("RspCode", errorResultMap.get("RspCode"));
                model.addAttribute("RspMsg", errorResultMap.get("RspMsg"));
                //共享服务端随机数
                model.addAttribute("serverRandom", sipDecryptor.generateServerRandom());
                //查询结果不显示
                model.addAttribute("hasNotLs", false);
                return CONSUMPTION_RECORD;
            }
        } catch (Exception e) {
            LogUtil.error("系统错误", e);
            redirectAttributes.addAttribute("errMsg", errorMsg);
            redirectAttributes.addAttribute("errCode", 9999);
            return FAIL_PAGE;
        }
    }

    /**
     * ajax校验卡bin
     *
     * @param cardNo 卡号
     * @return 校验结果   返回空串标识成功
     */
    @RequestMapping("ajax_CheckCardNo")
    @ResponseBody
    public String ajaxCheckCardNo(String cardNo) {
        return cardBinTypeService.verifyCard(cardNo);
    }

}
