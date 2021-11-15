package com.github.lnsane.web.common.config;

import cn.hutool.json.JSONUtil;
import com.github.lnsane.web.common.core.exception.FeignRequestException;
import com.github.lnsane.web.common.model.BaseResponse;
import feign.Client;
import feign.Request;
import feign.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author lnsane
 */
public class CustomFeignClient extends Client.Default {

    private final static Logger log = LoggerFactory.getLogger(CustomFeignClient.class);

    public CustomFeignClient(SSLSocketFactory sslContextFactory, HostnameVerifier hostnameVerifier) {
        super(sslContextFactory, hostnameVerifier);
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {

        Response response = super.execute(request, options);
        InputStream bodyStream = response.body().asInputStream();

        String responseBody = StreamUtils.copyToString(bodyStream, StandardCharsets.UTF_8);

        BaseResponse<?> baseResponse = JSONUtil.parse(responseBody).toBean(BaseResponse.class);
        if (!baseResponse.getOk()) {
            throw new FeignRequestException();
        }
        log.info(responseBody);

        return response.toBuilder().body(responseBody, StandardCharsets.UTF_8).build();
    }
}