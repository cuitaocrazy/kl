package com.yada.btg.web.core;

/**
 * @author zsy
 * @date 2020/1/14
 * Description:自定义参数错误异常
 */
public class ParamErrException extends RuntimeException {
    public ParamErrException(String message) {
        super(message);
    }
}
