package com.anderson.rinha_backend_2025_v1.infra.db.cache;

import com.anderson.rinha_backend_2025_v1.domain.services.IProcessorCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.anderson.rinha_backend_2025_v1.infra.util.Constants.LOCK_KEY;
import static com.anderson.rinha_backend_2025_v1.infra.util.Constants.PROCESSOR_KEY;

@Service
@RequiredArgsConstructor
public class ProcessorCacheService implements IProcessorCacheService {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void setCurrentProcessor(String processor) {
        redisTemplate.opsForValue().set(PROCESSOR_KEY, processor);
    }

    @Override
    public String getCurrentProcessor() {
        return redisTemplate.opsForValue().get(PROCESSOR_KEY);
    }

    @Override
    public Boolean setRedisLock(String lockValue) {
        return redisTemplate.opsForValue()
                .setIfAbsent(LOCK_KEY, lockValue, java.time.Duration.ofSeconds(4));
    }


} 