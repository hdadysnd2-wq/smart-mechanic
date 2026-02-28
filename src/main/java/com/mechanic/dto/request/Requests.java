package com.mechanic.dto.request;

import jakarta.validation.constraints.*;

/**
 * طلب التشخيص — المدخلات من تطبيق الموبايل
 */
public record DiagnoseRequest(
        @NotBlank(message = "رقم الشاسيه مطلوب")
        @Size(min = 17, max = 17, message = "رقم الشاسيه يجب أن يكون 17 خانة")
        @Pattern(regexp = "[A-HJ-NPR-Z0-9]{17}", message = "صيغة رقم الشاسيه غير صحيحة")
        String vin,

        @NotBlank(message = "كود العطل مطلوب")
        @Pattern(regexp = "[PBCUpbcu]\\d{4}", message = "صيغة كود العطل غير صحيحة (مثال: P0300)")
        String dtcCode
) {}


/**
 * طلب إضافة سيارة جديدة
 */
record CreateCarRequest(
        @NotBlank @Size(min = 17, max = 17)
        @Pattern(regexp = "[A-HJ-NPR-Z0-9]{17}")
        String vin,

        @NotBlank @Size(max = 50)
        String manufacturer,

        @NotBlank @Size(max = 50)
        String model,

        @Min(1886) @Max(2100)
        int year,

        @NotNull
        String fuelType,

        String transmission,
        double engineDisplacementCC,

        @Min(0) @Max(3000)
        int horsePower,

        String obdProtocol
) {}


/**
 * طلب تسجيل مستخدم
 */
record RegisterRequest(
        @NotBlank @Email(message = "البريد الإلكتروني غير صحيح")
        String email,

        @NotBlank @Size(min = 8, message = "كلمة المرور 8 أحرف على الأقل")
        String password,

        @NotBlank
        String fullName,

        String role
) {}


/**
 * طلب تسجيل الدخول
 */
record LoginRequest(
        @NotBlank @Email
        String email,

        @NotBlank
        String password
) {}
