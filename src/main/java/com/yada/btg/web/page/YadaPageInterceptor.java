package com.yada.btg.web.page;

import com.yada.btg.web.util.LogUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by zsy on 2019/10/17.
 * <p>
 * Description:分页查询拦截器
 */
@Component
public class YadaPageInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        boolean r = false;
        try {
            r = super.preHandle(request, response, handler);
        } catch (Exception e) {
            LogUtil.error("分页前置发生异常", e);
        }
        if (!r) {
            return r;
        } else if ("get".equalsIgnoreCase(request.getMethod())) {
            String key = request.getRequestURI();
            String queryString = request.getQueryString();
            Object cacheQueryString = request.getSession().getAttribute(key);
            LogUtil.info("key[" + key + "]queryString[" + queryString + "]cacheQueryString[" + cacheQueryString + "]");
            if (queryString != null && !queryString.contains("page=")) {
                response.sendRedirect(request.getRequestURI() + "?" + queryString + "&page=0");
                return false;
            } else if (queryString != null) {
                String pageNumber = request.getParameter("page");
                checkPageNumber(pageNumber);
                request.getSession().setAttribute(key, queryString);
                return true;
            } else if (cacheQueryString != null) {
                // 查询条件为空，但是session中对应的条件，则使用session中的条件
                response.sendRedirect(request.getRequestURI() + "?" + cacheQueryString.toString());
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String curQueryString = "";
        String firstQueryString = "";
        String preQueryString = "";
        String nextQueryString = "";
        String lastQueryString = "";
        if (request.getQueryString() == null) {
            Object page = modelAndView.getModel().get("page");
            if (page != null) {
                String[] test = page.toString().split(" ");
                long test3Value = new Long(test[3]);
                if (test3Value > 1) {
                    long pageSize = test3Value - 1;
                    curQueryString = "&page=0";
                    firstQueryString = "&page=0";
                    preQueryString = "&page=0";
                    nextQueryString = "&page=1";
                    lastQueryString = "&page=" + pageSize;
                } else {
                    curQueryString = "&page=0";
                    firstQueryString = "&page=0";
                    preQueryString = "&page=0";
                    nextQueryString = "&page=0";
                    lastQueryString = "&page=0";
                }
            }
        } else {
            int prePage;
            long totalPage;
            int nextPage;
            String queryString = request.getQueryString();
            String pageNumber = request.getParameter("page");
            if (pageNumber == null || pageNumber.isEmpty()) {
                pageNumber = "0";
            }
            checkPageNumber(pageNumber);
            // 上一页
            int curPage = new Integer(pageNumber);
            if (curPage > 1) {
                prePage = curPage - 1;
            } else {
                prePage = 0;
            }
            // 总页数
            if (modelAndView.getModel().get("page") != null) {
                Page<?> pages = (Page<?>) modelAndView.getModel().get("page");
                totalPage = pages.getTotalPages() - 1;
            } else {
                totalPage = 0;
            }
            // 下一页
            if (curPage < totalPage) {
                nextPage = curPage + 1;
            } else {
                nextPage = curPage;
            }
            curQueryString = queryString;
            firstQueryString = queryString.replace("page=" + curPage, "page=0");
            preQueryString = queryString.replace("page=" + curPage, "page=" + prePage);
            nextQueryString = queryString.replace("page=" + curPage, "page=" + nextPage);
            lastQueryString = queryString.replace("page=" + curPage, "page=" + totalPage);
        }
        request.setAttribute("firstQueryString", firstQueryString);
        request.setAttribute("preQueryString", preQueryString);
        request.setAttribute("nextQueryString", nextQueryString);
        request.setAttribute("lastQueryString", lastQueryString);
        request.setAttribute("curQueryString", curQueryString);
        super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 校验分页参数
     *
     * @param pageNumber 参数
     */
    private void checkPageNumber(String pageNumber) {
        String regexp = "(s&&[^\\f\\n\\r\\t\\v])*$+|^\\d{1,10}";
        boolean result = Pattern.compile(regexp).matcher(pageNumber).matches();
        if (!result) {
            throw new RuntimeException("分页参数格式错误！");
        }
    }
}
