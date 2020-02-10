package com.yada.btg.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zsy on 2019/12/27.
 * 美工用跳转页面
 */
@Controller
@RequestMapping("temp")
public class TempController {
    //自发卡业务介绍   temp/zifakayewujieshao
    @RequestMapping("zifakayewujieshao")
    public String zifakayewujieshao() {
        return "temp/zifakayewujieshao";
    }

    //购卡协议   temp/goukaxieyi
    @RequestMapping("goukaxieyi")
    public String goukaxieyi() {
        return "temp/goukaxieyi";
    }

    //互联网支付介绍   temp/hulianwangzhifujieshao
    @RequestMapping("hulianwangzhifujieshao")
    public String hulianwangzhifujieshao() {
        return "temp/hulianwangzhifujieshao";
    }

    //美点卡业务介绍   temp/meidiankayewujieshao
    @RequestMapping("meidiankayewujieshao")
    public String meidiankayewujieshao() {
        return "temp/meidiankayewujieshao";
    }

    //首付通卡业务介绍   temp/shoufutongkayewujieshao
    @RequestMapping("shoufutongkayewujieshao")
    public String shoufutongkayewujieshao() {
        return "temp/shoufutongkayewujieshao";
    }

    //预付费卡受理介绍   temp/yufufeikashoulijieshao
    @RequestMapping("yufufeikashoulijieshao")
    public String yufufeikashoulijieshao() {
        return "temp/yufufeikashoulijieshao";
    }

    //预付卡购卡章程   temp/yufukagoukazhangcheng
    @RequestMapping("yufukagoukazhangcheng")
    public String yufukagoukazhangcheng() {
        return "temp/yufukagoukazhangcheng";
    }

    //本部及网点售卡   temp/benbujiwangdianshouka
    @RequestMapping("benbujiwangdianshouka")
    public String benbujiwangdianshouka() {
        return "temp/benbujiwangdianshouka";
    }

    //法律声明   temp/falvshengming
    @RequestMapping("falvshengming")
    public String falvshengming() {
        return "temp/falvshengming";
    }

    //贵友卡介绍   temp/guiyoukajieshao
    @RequestMapping("guiyoukajieshao")
    public String guiyoukajieshao() {
        return "temp/guiyoukajieshao";
    }

    //合作伙伴   temp/hezuohuoban
    @RequestMapping("hezuohuoban")
    public String hezuohuoban() {
        return "temp/hezuohuoban";
    }

    //经营理念   temp/jingyinglinian
    @RequestMapping("jingyinglinian")
    public String jingyinglinian() {
        return "temp/jingyinglinian";
    }

    //联系我们   temp/lianxiwomen
    @RequestMapping("lianxiwomen")
    public String lianxiwomen() {
        return "temp/lianxiwomen";
    }

    //企业介绍   temp/qiyejieshao
    @RequestMapping("qiyejieshao")
    public String qiyejieshao() {
        return "temp/qiyejieshao";
    }

    //企业文化   temp/qiyewenhua
    @RequestMapping("qiyewenhua")
    public String qiyewenhua() {
        return "temp/qiyewenhua";
    }

    //西单卡介绍   temp/xidankajieshao
    @RequestMapping("xidankajieshao")
    public String xidankajieshao() {
        return "temp/xidankajieshao";
    }

    //招贤纳士   temp/zhaoxiannashi
    @RequestMapping("zhaoxiannashi")
    public String zhaoxiannashi() {
        return "temp/zhaoxiannashi";
    }

    //商户黄页 temp/shanghuhuangye
    @RequestMapping("/shanghuhuangye")
    public String query() {
        return "mer/queryResult";
    }
    //商户详情 temp/shanghuxiangqing
    @RequestMapping("/shanghuxiangqing")
    public String queryMer() {
        return "mer/merchantDetails";
    }
}
