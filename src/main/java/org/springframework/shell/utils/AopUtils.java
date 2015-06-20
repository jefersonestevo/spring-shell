package org.springframework.shell.utils;

import org.springframework.aop.framework.Advised;

public class AopUtils {

    @SuppressWarnings({"unchecked"})
    public static <T> T getTargetObject(Object proxy, Class<T> targetClass) {
        try {
            if (org.springframework.aop.support.AopUtils.isJdkDynamicProxy(proxy)) {
                return (T) ((Advised)proxy).getTargetSource().getTarget();
            } else {
                return (T) proxy; // expected to be cglib proxy then, which is simply a specialized class
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
