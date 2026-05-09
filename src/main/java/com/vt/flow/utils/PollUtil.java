package com.vt.flow.utils;

import com.vt.exception.WrapperException;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 轮询工具
 */
public class PollUtil {

    private PollUtil() {}

    public static <T> T poll(long timeoutMs, long intervalMs, Supplier<T> action, Predicate<T> finishCondition) {
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < timeoutMs) {
            T t = action.get();

            if (finishCondition.test(t)) return t;

            try {
                // 虚拟线程下的 sleep 是非阻塞的（对载体线程而言），非常安全
                Thread.sleep(intervalMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        throw new WrapperException("Timeout while waiting for result");
    }

}
