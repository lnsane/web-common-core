package com.github.lnsane.web.common.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author lnsane
 */
@Data
@Builder
public class RequestSupport {
    private static final long serialVersionUID = -5809782578272943999L;

    /**
     * 分布式追踪Id
     */
    private String trackId;
    /**
     * header
     */
    public static String X_L_HEADER_TRACK_ID = "x-l-header-track-id";

    /**
     * 分布式追踪步骤
     */
    private Integer trackIdIndex;

    /**
     * header
     */
    public static String X_L_HEADER_TRACK_ID_INDEX = "x-l-header-track-id-index";


    /**
     * 用户信息
     */
    private User user;
}
