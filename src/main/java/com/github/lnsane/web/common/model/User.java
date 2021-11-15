package com.github.lnsane.web.common.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author lnsane
 */
@Data
@Builder
public class User {
    /**
     * 用户id
     */
    private String userId;

    /**
     * header
     */
    public static String X_L_HEADER_USER_ID = "x-l-header-user-id";

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * header
     */
    public static String X_L_HEADER_TENANT_ID = "x-l-header-tenant-id";


}
