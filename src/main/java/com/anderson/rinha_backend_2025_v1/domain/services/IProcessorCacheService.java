package com.anderson.rinha_backend_2025_v1.domain.services;

public interface IProcessorCacheService {
    void setCurrentProcessor(String processor);
    String getCurrentProcessor();
}
