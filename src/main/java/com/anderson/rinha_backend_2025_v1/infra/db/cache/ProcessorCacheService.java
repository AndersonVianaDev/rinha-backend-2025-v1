package com.anderson.rinha_backend_2025_v1.infra.db.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static com.anderson.rinha_backend_2025_v1.infra.util.Constants.PROCESSOR_KEY;

@Service
@RequiredArgsConstructor
public class ProcessorCacheService {
    private final StringRedisTemplate redisTemplate;

    public void setCurrentProcessor(String processor) {
        redisTemplate.opsForValue().set(PROCESSOR_KEY, processor);
    }

    public String getCurrentProcessor() {
        return redisTemplate.opsForValue().get(PROCESSOR_KEY);
    }
} 