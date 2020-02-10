package com.yada.btg.web.core;

import com.yada.btg.web.entity.MsgContext;
import com.yada.btg.web.service.CommHandleService;
import com.yada.btg.web.service.MsgContextService;
import com.yada.btg.web.util.CommUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author yn
 * @date 2019/12/30
 * Description:全局数据绑定
 */
@ControllerAdvice
public class GlobalDataBinding {
    private final MsgContextService msgContextService;
    private final CommHandleService commHandleService;

    @Autowired
    public GlobalDataBinding(MsgContextService msgContextService, CommHandleService commHandleService) {
        this.msgContextService = msgContextService;
        this.commHandleService = commHandleService;
    }

    /**
     * 获取公共全局数据（大实体类）
     */
    @ModelAttribute(name = "msg")
    public MsgContext msgHandler() {
        return msgContextService.getMsgContext();
    }


    /**
     * 查询二级菜单数据及头部图片
     *
     * @param msg       缓存数据
     * @param menuOneId 一级菜单Id
     * @param menuTwoId 二级菜单Id
     * @param request   HttpServletRequest
     * @param model     Model
     */
    @ModelAttribute
    public void preposition(@ModelAttribute(name = "msg") MsgContext msg,
                            String menuOneId,
                            String menuTwoId,
                            HttpServletRequest request,
                            Model model) throws ParamErrException {
        //配置不用获取二级菜单信息的路径
        List<String> paths = Arrays.asList(msg.getMenuPath().split(","));

        String homePath = "/";
        String homePathRedirect = "/home/index";
        if (Objects.isNull(menuOneId) && (homePath.equals(request.getServletPath())||homePathRedirect.equals(request.getServletPath()))) {
            menuOneId = msg.getHomeMenuId();
        } else if (!(paths.contains(request.getServletPath()) || request.getServletPath().contains("/ajax_"))) {
            commHandleService.getMenuTwo(msg, menuTwoId, menuOneId, model, request);
        }

        model.addAttribute("imgs", CommUtil.getMenuImg(msg, menuOneId, menuTwoId));
    }
}
