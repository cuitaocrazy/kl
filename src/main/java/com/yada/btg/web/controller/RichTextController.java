package com.yada.btg.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zsy
 * @date 2019/12/18
 * Description:富文本框类处理controller
 */
@Controller
@RequestMapping("/richText")
public class RichTextController {

    @RequestMapping("/text")
    public String text() {
        return "text/richTextPage";
    }
}
