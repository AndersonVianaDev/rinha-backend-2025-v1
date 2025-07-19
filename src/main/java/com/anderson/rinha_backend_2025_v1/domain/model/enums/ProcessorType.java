package com.anderson.rinha_backend_2025_v1.domain.model.enums;

import com.anderson.rinha_backend_2025_v1.infra.exceptions.InvalidDataException;

public enum ProcessorType {
    DEFAULT,
    FALLBACK,
    FAILURE;

    public static ProcessorType getType(String value) {
        for (ProcessorType type : ProcessorType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }

        throw new InvalidDataException("Invalid type");
    }
}
