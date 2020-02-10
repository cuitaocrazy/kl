package com.yada.btg.web.core;

import com.yada.btg.web.util.LogUtil;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zsy
 * @date 2019/12/30
 * Description:全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 全局异常处理
     *
     * @param model     model
     * @param throwable 异常
     * @return 异常页面
     */
    @ExceptionHandler(Throwable.class)
    public String defaultErrorHandler(Model model, Throwable throwable, HttpServletRequest httpServletRequest) {
        LogUtil.error("程序发生未知异常,请求是[" + httpServletRequest.getRequestURI() + "],参数是[" + httpServletRequest.getQueryString() + "]", throwable);
        if (throwable instanceof ParamErrException) {
            model.addAttribute("status", "404");
            model.addAttribute("error", "页面找不到");
            return "error";
        } else if (throwable instanceof Exception) {
            model.addAttribute("errCode", "9999");
            model.addAttribute("errMsg", "系统发生错误");
        }
        return "globalError";
    }
}
