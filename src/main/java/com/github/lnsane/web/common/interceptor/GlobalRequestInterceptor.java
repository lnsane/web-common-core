package com.github.lnsane.web.common.interceptor;

import cn.hutool.core.util.StrUtil;
import com.github.lnsane.web.common.content.PreRequestHandle;
import com.github.lnsane.web.common.content.RequestContext;
import com.github.lnsane.web.common.core.exception.BadRequestException;
import com.github.lnsane.web.common.model.ExceptionFlag;
import com.github.lnsane.web.common.model.RequestSupport;
import com.github.lnsane.web.common.model.User;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lnsane
 */
@Order(value = Integer.MIN_VALUE)
public class GlobalRequestInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ExceptionFlag exceptionFlag = ExceptionFlag.builder().build();
        exceptionFlag.setStartTime(System.currentTimeMillis());
        PreRequestHandle.set(exceptionFlag);
        String trackId = request.getHeader(RequestSupport.X_L_HEADER_TRACK_ID);
        String tenantId = request.getHeader(User.X_L_HEADER_TENANT_ID);
        String userId = request.getHeader(User.X_L_HEADER_USER_ID);
        String index = request.getHeader(RequestSupport.X_L_HEADER_TRACK_ID_INDEX);
        if (StrUtil.hasBlank(tenantId,trackId,userId,index)) {
            throw new BadRequestException();
        }
        RequestContext.set(RequestSupport.builder().user(User.builder().userId(userId).tenantId(tenantId).build()).trackId(trackId).trackIdIndex(Integer.parseInt(index)).build());
        return Boolean.TRUE;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    private ExceptionFlag resolveRequest(HttpServletRequest request) {
        return null;
    }
}
