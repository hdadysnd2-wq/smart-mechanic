package com.mechanic.dto.response;

public record VehicleInfo(
        String vin,
        String displayName,
        String manufacturer,
        String model,
        int year,
        String fuelType,
        String obdProtocol
) {}
