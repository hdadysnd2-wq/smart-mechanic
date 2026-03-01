package com.mechanic.dto.response;

public record MeasurementResponse(
        String measurementType,
        String unit,
        double minValue,
        double maxValue,
        double targetValue,
        String tool
) {}
