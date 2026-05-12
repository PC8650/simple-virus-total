package com.vt.flow.component;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.vt.flow.dto.CacheDto;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

/**
 * 缓存管理
 */
@Component
public class CacheManager {

    private static final CaffeineCacheManager MANAGER;

    private static final String CACHE_NAME = "flow_scan";

    private static final Duration EXPIRE = Duration.ofMinutes(20);

    static {
        MANAGER = new CaffeineCacheManager();
        MANAGER.setCacheNames(List.of(CACHE_NAME));
        MANAGER.setCaffeine(Caffeine.newBuilder()
                //初始容量
                .initialCapacity(4)
                //最大容量
                .maximumSize(16)
                //单位时间内没被 读/写 则过期
                .expireAfterAccess(EXPIRE)
        );
    }

    public void put(CacheDto cache) {
        if (Objects.isNull(cache) || !StringUtils.hasText(cache.getKey())) return;
        Objects.requireNonNull(MANAGER.getCache(CACHE_NAME)).put(cache.getKey(), cache);
    }

    public CacheDto get(Object key) {
        return Objects.requireNonNull(MANAGER.getCache(CACHE_NAME)).get(key, CacheDto.class);
    }

}
