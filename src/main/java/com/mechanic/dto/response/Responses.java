package com.mechanic.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        String errorCode,
        LocalDateTime timestamp
) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "تمت العملية بنجاح", data, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return new ApiResponse<>(false, message, null, errorCode, LocalDateTime.now());
    }
}


public record DiagnosticResponse(
        VehicleInfo vehicle,
        FaultInfo fault,
        String repairStrategy,
        List<String> safetyPrerequisites,
        List<String> requiredTools,
        List<StepResponse> steps,
        List<ComplaintResponse> relatedComplaints,
        int estimatedTimeMinutes,
        boolean requiresProgramming
) {}


public record VehicleInfo(
        String vin,
        String displayName,
        String manufacturer,
        String model,
        int year,
        String fuelType,
        String obdProtocol
) {}


public record FaultInfo(
        String code,
        String shortDescription,
        String detailedDescription,
        String category,
        String severity,
        String systemLabel,
        List<String> possibleCauses,
        List<String> componentsToCheck,
        double averageRepairCostUSD
) {}


public record StepResponse(
        int stepNumber,
        String title,
        String description,
        String imageUrl,
        String videoUrl,
        List<String> toolsUsed,
        String warningNote,
        String torqueSpec,
        MeasurementResponse measurement,
        int estimatedMinutes,
        boolean isVerificationStep
) {}


public record MeasurementResponse(
        String measurementType,
        String unit,
        double minValue,
        double maxValue,
        double targetValue,
        String tool
) {}


public record ComplaintResponse(
        String complaintPattern,
        int frequencyPercent,
        String typicalCondition,
        String resolution
) {}


public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        String email,
        String fullName,
        String role
) {
    public static AuthResponse of(String access, String refresh, long exp,
                                   String email, String name, String role) {
        return new AuthResponse(access, refresh, "Bearer", exp, email, name, role);
    }
}


public record CarSummaryResponse(
        String id,
        String vin,
        String displayName,
        String manufacturer,
        String model,
        int year,
        String fuelType,
        int horsePower
) {}