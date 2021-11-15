package com.github.lnsane.web.common.content;

import com.github.lnsane.web.common.model.ExceptionFlag;

/**
 * @author lnsane
 */
public class PreRequestHandle {

    private static final ThreadLocal<ExceptionFlag> preRequestHandle = new InheritableThreadLocal<>();

    public static void set(ExceptionFlag startTime) {
        preRequestHandle.set(startTime);
    }


    public static ExceptionFlag get() {
        return preRequestHandle.get();
    }

    public static void remove() {
        preRequestHandle.remove();
    }
}
