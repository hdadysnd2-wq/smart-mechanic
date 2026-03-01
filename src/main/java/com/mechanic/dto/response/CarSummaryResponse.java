package com.mechanic.dto.response;
public record CarSummaryResponse(String id, String vin, String displayName, String manufacturer, String model, int year, String fuelType, int horsePower) {}
