package com.github.lnsane.web.common.params;

import cn.hutool.core.bean.BeanUtil;

public interface BaseParams<T> {
    /**
     * 更新操作
     *
     * @param params
     */
    default void update(T params) {
        BeanUtil.copyProperties(this, params);
    }
}
