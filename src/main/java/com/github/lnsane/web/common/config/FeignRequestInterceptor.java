package com.github.lnsane.web.common.config;

import com.github.lnsane.web.common.content.RequestContext;
import com.github.lnsane.web.common.model.RequestSupport;
import com.github.lnsane.web.common.model.User;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

/**
 * @author lnsane
 */
@Component
@ConditionalOnClass(value = RequestInterceptor.class)
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestSupport requestSupport = RequestContext.get();
        requestTemplate.header(User.X_L_HEADER_TENANT_ID,requestSupport.getUser().getTenantId());
        requestTemplate.header(User.X_L_HEADER_USER_ID,requestSupport.getUser().getUserId());
        requestTemplate.header(RequestSupport.X_L_HEADER_TRACK_ID,requestSupport.getTrackId());
        requestTemplate.header(RequestSupport.X_L_HEADER_TRACK_ID_INDEX,String.valueOf(requestSupport.getTrackIdIndex() + 1));
    }
}
