package com.github.lnsane.web.common.demo;

/**
 * @author lnsane
 */
public enum EnumsUser {
    TT(1),
    PP(2);

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    private Integer code;

    EnumsUser(int i) {
        this.code = i;
    }
}
