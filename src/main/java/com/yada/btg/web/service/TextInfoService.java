package com.yada.btg.web.service;

import com.yada.btg.web.entity.*;
import com.yada.btg.web.repository.TextInfoRepository;
import com.yada.btg.web.util.CommUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author zsy
 * @date 2019/12/25
 * 列表类处理service
 */
@Service
public class TextInfoService {
    private final TextInfoRepository textInfoRepository;
    private final CommHandleService commHandleService;

    @Autowired
    public TextInfoService(TextInfoRepository textInfoRepository,
                           CommHandleService commHandleService) {
        this.textInfoRepository = textInfoRepository;
        this.commHandleService = commHandleService;
    }


    /**
     * 列表类菜单处理
     *
     * @param msgContext 数据
     * @param menuTwoId  二级菜单id
     * @param menuOneId  一级菜单id
     * @param model      model
     * @param pageable   pageable
     */
    public void handle(MsgContext msgContext,
                       String menuTwoId,
                       String menuOneId,
                       Model model,
                       Pageable pageable,
                       HttpServletRequest httpServletRequest) {
        MenuTwo menuTwo = commHandleService.getMenuTwo(msgContext, menuTwoId, menuOneId, model, httpServletRequest);
        //根据菜单栏目去查询文章列表
        ColumnInfo columnInfo = menuTwo.getColumnType();
        Page<TextInfo> textInfoList = textInfoRepository.findAllByTextType(columnInfo, pageable);
        model.addAttribute("size", textInfoList.getSize());
        model.addAttribute("contents", textInfoList.getContent());
        model.addAttribute("page", textInfoList);
    }


    /**
     * 查询文章详情方法
     *
     * @param msgContext 数据
     * @param menuTwoId  二级菜单id
     * @param menuOneId  一级菜单id
     * @param model      model
     */
    public void queryTextInfo(MsgContext msgContext,
                              String menuTwoId,
                              String menuOneId,
                              String textId,
                              Model model,
                              HttpServletRequest httpServletRequest) {
        commHandleService.getMenuTwo(msgContext, menuTwoId, menuOneId, model, httpServletRequest);
        TextInfo textInfo = textInfoRepository.findById(textId).get();
        model.addAttribute("textInfo", textInfo);
    }

    /**
     * 获取首页推荐新闻
     *
     * @param msg       数据
     * @param menuOneId 一级菜单Id
     * @return 二级菜单列表
     */
    public List<MenuTwo> getRecomendNews(MsgContext msg, String menuOneId) {
        Map<Long, MenuTwo> map = new HashMap<>(4);
        MenuOne menuOne = CommUtil.getMenuOne(msg, menuOneId);
        for (MenuTwo menuTwo : menuOne.getMenuTwoList()) {
            ColumnInfo columnInfo = menuTwo.getColumnType();
            if (map.size() < 4 && Objects.nonNull(columnInfo) && !map.containsKey(columnInfo.getColumnId())) {
                List<TextInfo> list = textInfoRepository.findTop6ByTextTypeOrderByUpdateTimeDesc(columnInfo);
                columnInfo.setTextInfoList(Objects.isNull(list) ? new ArrayList<>() : list);
                menuTwo.setColumnType(columnInfo);
                map.put(columnInfo.getColumnId(), menuTwo);
            }
        }

        return new ArrayList<>(map.values());
    }
}
