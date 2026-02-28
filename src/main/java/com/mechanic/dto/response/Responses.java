package com.mechanic.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ══════════════════════════════════════════════════════
 *  استجابة موحّدة لجميع الـ API Endpoints
 *  تُضيف: status, message, timestamp لكل رد
 * ══════════════════════════════════════════════════════
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        String errorCode,
        LocalDateTime timestamp
) {
    // ✅ استجابة ناجحة
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "تمت العملية بنجاح", data, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data, null, LocalDateTime.now());
    }

    // ❌ استجابة خطأ
    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return new ApiResponse<>(false, message, null, errorCode, LocalDateTime.now());
    }
}


/**
 * استجابة التشخيص الكاملة
 */
record DiagnosticResponse(
        // معلومات السيارة
        VehicleInfo vehicle,

        // معلومات العطل
        FaultInfo fault,

        // الاستراتيجية المُختارة
        String repairStrategy,

        // تحذيرات السلامة
        List<String> safetyPrerequisites,

        // الأدوات المطلوبة
        List<String> requiredTools,

        // الخطوات بصيغة JSON لتطبيق الموبايل
        List<StepResponse> steps,

        // الشكاوى الشائعة المرتبطة
        List<ComplaintResponse> relatedComplaints,

        // الوقت التقديري
        int estimatedTimeMinutes,

        // هل تحتاج برمجة بعد الإصلاح؟
        boolean requiresProgramming
) {}


/**
 * معلومات السيارة في الاستجابة
 */
record VehicleInfo(
        String vin,
        String displayName,
        String manufacturer,
        String model,
        int year,
        String fuelType,
        String obdProtocol
) {}


/**
 * معلومات العطل في الاستجابة
 */
record FaultInfo(
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


/**
 * خطوة الإصلاح — المهيكلة لتطبيق الموبايل
 */
record StepResponse(
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


/**
 * مواصفات القياس
 */
record MeasurementResponse(
        String measurementType,
        String unit,
        double minValue,
        double maxValue,
        double targetValue,
        String tool
) {}


/**
 * شكوى مستهلك
 */
record ComplaintResponse(
        String complaintPattern,
        int frequencyPercent,
        String typicalCondition,
        String resolution
) {}


/**
 * استجابة JWT بعد تسجيل الدخول
 */
record AuthResponse(
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


/**
 * بيانات السيارة في القائمة
 */
record CarSummaryResponse(
        String id,
        String vin,
        String displayName,
        String manufacturer,
        String model,
        int year,
        String fuelType,
        int horsePower
) {}
