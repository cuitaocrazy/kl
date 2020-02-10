package com.yada.btg.web.util;

import com.yada.btg.web.entity.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zsy on 2019/12/25.
 * 获取数据的公用工具类
 */
public class CommUtil {
    /**
     * 从事先获取的数据中获取对应的菜单图片信息
     *
     * @param msgContext 数据
     * @param menuOneId  一级菜单id
     * @param menuTwoId  二级菜单id
     * @return 菜单图片数据
     */
    public static List<MenuImgExt> getMenuImg(MsgContext msgContext, String menuOneId, String menuTwoId) {
        List<MenuImgExt> menuImgList = msgContext.getMenuImgList();
        List<MenuImgExt> menuImgs = new ArrayList<>();
        for (MenuImgExt o : menuImgList) {
            if (o.getMenuOne().equals(menuOneId) && (Objects.isNull(o.getMenuTwo()) && Objects.isNull(menuTwoId) || o.getMenuTwo().equals(menuTwoId))) {
                menuImgs.add(o);
            }
        }
        return menuImgs;
    }

    /**
     * 从事先获取的数据中获取一级菜单数据（会把对应的关联数据也取出来）
     *
     * @param msgContext 数据
     * @param menuOneId  一级菜单id
     * @return 一级菜单数据
     */
    public static MenuOne getMenuOne(MsgContext msgContext, String menuOneId) {
        List<MenuOne> menuTwoList = msgContext.getMenuOneList();
        MenuOne menuOne = null;
        for (MenuOne menuOne1 : menuTwoList) {
            if (menuOne1.getMenuId().equals(menuOneId)) {
                menuOne = menuOne1;
                break;
            }
        }
        return menuOne;
    }

    /**
     * 从事先获取的数据中获取二级菜单数据（会把对应的关联数据也取出来）
     *
     * @param msgContext 数据
     * @param menuTwoId  二级菜单id
     * @return 二级菜单数据
     */
    public static MenuTwo getMenuTwo(MsgContext msgContext, String menuTwoId) {
        List<MenuTwo> menuTwoList = msgContext.getMenuTwoList();
        MenuTwo menuTwo = null;
        for (MenuTwo menuTwo1 : menuTwoList) {
            if (menuTwo1.getMenuId().equals(menuTwoId)) {
                menuTwo = menuTwo1;
                break;
            }
        }
        return menuTwo;
    }

    /**
     * 获取微信信息里面的一级菜单名称
     *
     * @param menuOneId   一级菜单id
     * @param menuOneList 一级菜单集合
     * @return 一级菜单名称
     */
    public static String getMenuOneName(String menuOneId, List<MenuOne> menuOneList) {
        String menuOneName = null;
        for (MenuOne menuOne : menuOneList) {
            if (menuOne.getMenuId().equals(menuOneId)) {
                menuOneName = menuOne.getMenuName();
                break;
            }
        }
        return menuOneName;
    }

    /**
     * 获取以及菜单列表
     *
     * @param menuOneList 一级菜单类别
     * @return 一级菜单名称
     */
    public static List<MenuOne> getHeaderMenuList(List<MenuOne> menuOneList) {
        List<MenuOne> menuOnes = new ArrayList<>();
        if (Objects.nonNull(menuOneList)) {
            for (MenuOne menuOne : menuOneList) {
                if (menuOne.getHeaderFlag() == 1) {
                    menuOne.getMenuTwoList().sort(Comparator.comparing(MenuTwo::getMenuWeight).reversed());
                    menuOnes.add(menuOne);
                }
            }
        }
        return menuOnes;
    }

    /**
     * 生成msgId
     *
     * @return msgId
     */
    public static String getMsgId() {
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Random random = new Random();
        random.nextInt(6);
        StringBuilder captcha = new StringBuilder();
        for (int a = 0; a <= 5; a++) {
            captcha.append(random.nextInt(10));
        }
        return date + captcha.toString();
    }

    /**
     * 对字典list集合按照value值进行排序
     *
     * @param dictItemInfoList 集合
     * @return 排序后的集合
     */
    public static List<DictInfo> sortInfo(List<DictInfo> dictItemInfoList) {
        dictItemInfoList.sort(Comparator.comparing(DictInfo::getItemValue));
        return dictItemInfoList;
    }
}


