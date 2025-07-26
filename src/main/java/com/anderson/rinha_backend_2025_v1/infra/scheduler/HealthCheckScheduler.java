package com.anderson.rinha_backend_2025_v1.infra.scheduler;

import com.anderson.rinha_backend_2025_v1.domain.model.enums.ProcessorType;
import com.anderson.rinha_backend_2025_v1.domain.services.IPaymentProcessorDefaultService;
import com.anderson.rinha_backend_2025_v1.domain.services.IPaymentProcessorFallbackService;
import com.anderson.rinha_backend_2025_v1.domain.services.IProcessorCacheService;
import com.anderson.rinha_backend_2025_v1.domain.model.dtos.ServiceHealthDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.util.UUID;

import static com.anderson.rinha_backend_2025_v1.infra.util.Constants.LOCK_KEY;

@Component
@RequiredArgsConstructor
public class HealthCheckScheduler {

    private final IPaymentProcessorDefaultService paymentProcessorDefault;
    private final IPaymentProcessorFallbackService paymentProcessorFallback;
    private final IProcessorCacheService processorCacheService;
    private final StringRedisTemplate redisTemplate;

    @Value("${processor.default.margin:250}")
    private int defaultMargin;

    @Scheduled(fixedRate = 5000)
    public void healthCheck() {
        String lockValue = UUID.randomUUID().toString();
        Boolean acquired = redisTemplate.opsForValue()
                .setIfAbsent(LOCK_KEY, lockValue, java.time.Duration.ofSeconds(4));
        if (acquired == null || !acquired) {
            return;
        }

        final ServiceHealthDTO serviceHealthDefault = paymentProcessorDefault.serviceHealth();
        final ServiceHealthDTO serviceHealthFallback = paymentProcessorFallback.serviceHealth();

        final boolean failingDefault = serviceHealthDefault.failing();
        final boolean failingFallback = serviceHealthFallback.failing();

        final int minResponseDefault = serviceHealthDefault.minResponseTime();
        final int minResponseFallback = serviceHealthFallback.minResponseTime();

        final String typeDefault = ProcessorType.DEFAULT.name();
        final String typeFallback = ProcessorType.FALLBACK.name();

        if (!failingDefault && failingFallback) {
            processorCacheService.setCurrentProcessor(typeDefault);
            return;
        }

        if (failingDefault && !failingFallback) {
            processorCacheService.setCurrentProcessor(typeFallback);
            return;
        }

        if (!failingDefault && !failingFallback) {
            if ((minResponseDefault < minResponseFallback) || (minResponseDefault == minResponseFallback)) {
                processorCacheService.setCurrentProcessor(typeDefault);
                return;
            }

            int diff = minResponseDefault - minResponseFallback;
            processorCacheService.setCurrentProcessor((diff > defaultMargin) ? typeFallback : typeDefault);
            return;
        }

        processorCacheService.setCurrentProcessor(ProcessorType.FAILURE.name());
    }
}
