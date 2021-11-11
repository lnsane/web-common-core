package com.github.lnsane.web.common.utils;

import org.apache.commons.lang3.ArrayUtils;

import javax.servlet.http.HttpServletRequest;

import static cn.hutool.extra.servlet.ServletUtil.getClientIPByHeader;

/**
 * @author lnsane
 */
public class ServletUtils {

    /**
     * 获取客户端IP.
     *
     * <p>
     * 默认检测的Header:
     *
     * <pre>
     * 1、X-Forwarded-For
     * 2、X-Real-IP
     * 3、Proxy-Client-IP
     * 4、WL-Proxy-Client-IP
     * </pre>
     *
     * <p>
     * otherHeaderNames参数用于自定义检测的Header<br>
     * 需要注意的是，使用此方法获取的客户IP地址必须在Http服务器（例如Nginx）中配置头信息，否则容易造成IP伪造。
     * </p>
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @param otherHeaderNames 其他自定义头文件，通常在Http服务器（例如Nginx）中配置
     * @see <a href="https://github.com/dromara/hutool/blob/v5-master/hutool-extra/src/main/java/cn/hutool/extra/servlet/ServletUtil.java">查看来源</a>
     * @return IP地址
     */
    public static String getClientIP(HttpServletRequest request, String... otherHeaderNames) {
        String[] headers = {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
        if (ArrayUtils.isNotEmpty(otherHeaderNames)) {
            headers = ArrayUtils.addAll(headers, otherHeaderNames);
        }
        String clientIPByHeader = getClientIPByHeader(request, headers);
        if (clientIPByHeader.equals("0:0:0:0:0:0:0:1")) {
            return "localhost";
        }

        return clientIPByHeader;
    }
}
