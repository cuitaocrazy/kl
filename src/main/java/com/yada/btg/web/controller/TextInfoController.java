package com.yada.btg.web.controller;

import com.yada.btg.web.entity.MsgContext;
import com.yada.btg.web.service.TextInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zsy
 * @date 2019/12/18
 * Description:列表类数据处理controller
 */
@Controller
@RequestMapping("list")
public class TextInfoController {
    /**
     * 新闻资讯菜单名称
     */
    @Value("${param.menu.newsId}")
    String newsMenuId;
    //文章列表页面路径
    private static final String LIST = "list/listPage";
    //文章详情页面路径
    private static final String DETAILS = "list/newsDetails";
    //新闻资讯页面路径
    private static final String NEWS_INFO = "list/newsList";

    private final TextInfoService textInfoService;

    @Autowired
    public TextInfoController(TextInfoService textInfoService) {
        this.textInfoService = textInfoService;
    }

    /**
     * 新闻资讯页面
     *
     * @param model model
     * @param msg   数据
     * @return 新闻资讯页面
     */
    @RequestMapping("newsInfo")
    public String newsInfo(Model model, @ModelAttribute(name = "msg") MsgContext msg) {
        model.addAttribute("newsMenuId", newsMenuId);
        model.addAttribute("news", textInfoService.getRecomendNews(msg, newsMenuId));
        model.addAttribute("isShowHeardImg", "true");
        return NEWS_INFO;
    }

    /**
     * 获取文章列表数据（分页）
     *
     * @param model     model
     * @param msg       数据
     * @param menuOneId 一级菜单id
     * @param menuTwoId 二级菜单id
     * @param pageable  pageable
     * @return 列表页面
     */
    @RequestMapping("queryTextList")
    public String textList(Model model,
                           @ModelAttribute(name = "msg") MsgContext msg,
                           String menuOneId,
                           String menuTwoId,
                           @PageableDefault(sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                           HttpServletRequest httpServletRequest) {
        textInfoService.handle(msg, menuTwoId, menuOneId, model, pageable, httpServletRequest);
        return LIST;
    }

    /**
     * 查询文章详情数据
     *
     * @param model     model
     * @param msg       数据
     * @param textId    文章id
     * @param menuOneId 一级菜单id
     * @param menuTwoId 二级菜单id
     * @return 文章详情页面
     */
    @RequestMapping("textInfo")
    public String textInfo(Model model,
                           @ModelAttribute(name = "msg") MsgContext msg,
                           String textId,
                           String menuOneId,
                           String menuTwoId,
                           HttpServletRequest httpServletRequest) {
        textInfoService.queryTextInfo(msg, menuTwoId, menuOneId, textId, model, httpServletRequest);
        return DETAILS;
    }

}
