package com.github.lnsane.web.common.content;


import com.github.lnsane.web.common.model.RequestSupport;

/**
 * @author 王存露
 * @date 2021/3/15
 */
public class RequestContext {
    /**
     * ThreadLocal 适用于每个线程需要自己独立的实例且该实例需要在多个方法中被使用（相同线程数据共享），也就是变量在线程间隔离（不同的线程数据隔离）而在方法或类间共享的场景。
     * 利用ThreadLocal管理登录用户信息实现随用随取
     * 每个请求都会对应一个线程，这个ThreadLocal就是这个线程使用过程中的一个变量，该变量为其所属线程所有，各个线程互不影响
     * 在一个请求中，所有调用的方法都在同一个线程中去处理，这样就实现了在任何地方都可以获取到用户信息了
     */
    private static final ThreadLocal<RequestSupport> loginUserLocal = new InheritableThreadLocal<>();

    public static void set(RequestSupport userInfo) {
        loginUserLocal.set(userInfo);
    }


    public static RequestSupport get() {
        return loginUserLocal.get();
    }

    public static void remove() {
        loginUserLocal.remove();
    }
}
