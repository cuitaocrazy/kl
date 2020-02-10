package com.yada.btg.web.controller;

import com.yada.btg.web.entity.MsgContext;
import com.yada.btg.web.service.TextInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Administrator
 */
@Controller
public class HomeController {

    /**
     * 企业微信页面路径
     */
    private static final String WECHAT = "enterpriseWeChat";
    /**
     * 错误页面路径
     */
    private static final String GLOBAL_ERROR = "globalError";
    private final TextInfoService textInfoService;

    public HomeController(TextInfoService textInfoService) {
        this.textInfoService = textInfoService;
    }

    /**
     * 首页访问地址（因为首页富文本框图片路径问题所以统一重定向到/home/index）
     *
     * @return 跳转地址
     */
    @GetMapping({"/"})
    public String index() {
        return "redirect:home/index";
    }

    /**
     * 首页地址
     *
     * @param msg   上下文
     * @param model model
     * @return 跳转页面
     */
    @GetMapping({"/home/index"})
    public String home(@ModelAttribute(name = "msg") MsgContext msg,
                       Model model) {
        model.addAttribute("news", textInfoService.getRecomendNews(msg, msg.getNewsMenuId()));
        return "home";
    }

    @GetMapping("/spontaneousCard")
    public String spontaneousCard() {
        return "spontaneousCard";
    }

    /**
     * 企业微信跳转路径
     *
     * @return 企业微信路径
     */
    @RequestMapping("enterpriseWeChat")
    public String enterpriseWeChat() {
        return WECHAT;
    }

    /**
     * 跳转通用异常页面
     *
     * @return 页面路径
     */
    @RequestMapping("/globalError")
    public String globalError(Model model, String errCode, String errMsg) {
        model.addAttribute("errCode", errCode);
        model.addAttribute("errMsg", errMsg);
        return GLOBAL_ERROR;
    }

}
