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

@Component
@RequiredArgsConstructor
public class HealthCheckScheduler {

    private final IPaymentProcessorDefaultService paymentProcessorDefault;
    private final IPaymentProcessorFallbackService paymentProcessorFallback;
    private final IProcessorCacheService processorCacheService;

    @Value("${processor.default.margin:10}")
    private int defaultMargin;

    @Scheduled(fixedRate = 5000)
    public void healthCheck() {
        try {
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
                /*
                 *  DEFAULT | FALLBACK | DIFF  >  DEFAULT_MARGIN | BOOLEAN
                 *   120    |   100    |  20   >       10        |  TRUE  = FALLBACK
                 *   100    |   120    | -20   >       10        |  FALSE = DEFAULT
                 *    92    |   100    | -8    >       10        |  FALSE = DEFAULT
                 *   100    |   92     |  8    >       10        |  FALSE = DEFAULT
                 *   103    |   92     |  11   >       10        |  TRUE = FALLBACK
                 */
            }

            processorCacheService.setCurrentProcessor(ProcessorType.FAILURE.name());
        } catch (Exception e) {
            return;
        }
    }
}
