package com.yada.btg.web.service;

import com.yada.btg.web.core.ParamErrException;
import com.yada.btg.web.entity.MenuOne;
import com.yada.btg.web.entity.MenuTwo;
import com.yada.btg.web.entity.MsgContext;
import com.yada.btg.web.util.CommUtil;
import com.yada.btg.web.util.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zsy
 * @date 2020/1/9
 * Description:一些公共处理的抽取
 */
@Service
public class CommHandleService {
    /**
     * 获取二级菜单数据
     *
     * @param msgContext 数据
     * @param menuTwoId  二级菜单id
     * @param menuOneId  一级菜单id
     * @param model      model
     * @return 二级菜单数据
     */
    public MenuTwo getMenuTwo(MsgContext msgContext,
                              String menuTwoId,
                              String menuOneId,
                              Model model,
                              HttpServletRequest httpServletRequest) {
        try {
            MenuTwo menuTwo = CommUtil.getMenuTwo(msgContext, menuTwoId);
            model.addAttribute("menuTwo", menuTwo);
            model.addAttribute("menuOneId", menuOneId);
            model.addAttribute("menuTwoId", menuTwoId);
            assert menuTwo != null;
            List<MenuOne> menuOneList = menuTwo.getMenuOneList();
            String menuOneName = CommUtil.getMenuOneName(menuOneId, menuOneList);
            model.addAttribute("menuOnePath", menuOneName);
            model.addAttribute("menuTwoPath", menuTwo.getMenuName());
            return menuTwo;
        } catch (NullPointerException n) {
            LogUtil.error("获取菜单数据发生错误，请求是[" + httpServletRequest.getRequestURI() + "],参数是[" + httpServletRequest.getQueryString() + "]", n);
            throw new ParamErrException(n.getMessage());
        }
    }
}
