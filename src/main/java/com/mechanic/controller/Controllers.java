package com.mechanic.controller;

import com.mechanic.dto.request.DiagnoseRequest;
import com.mechanic.dto.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * ══════════════════════════════════════════════════════
 *  التشخيص — الـ Endpoint الرئيسي
 *  POST /diagnose → يحلّل VIN + DTC ويرجع خطوات JSON
 * ══════════════════════════════════════════════════════
 */
@RestController
@RequestMapping("/diagnose")
@Tag(name = "التشخيص", description = "تشخيص الأعطال وإرجاع خطوات الإصلاح")
@SecurityRequirement(name = "Bearer Authentication")
public class DiagnoseController {

    /**
     * POST /api/v1/diagnose
     * الإدخال: VIN + DTC Code
     * الإخراج: تقرير كامل مع خطوات JSON لتطبيق الموبايل
     *
     * مثال الطلب:
     * {
     *   "vin": "1HGCM82633A123456",
     *   "dtcCode": "P0300"
     * }
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "تشخيص عطل السيارة",
               description = "يستقبل رقم الشاسيه وكود العطل ويرجع دليل الإصلاح الكامل بصيغة JSON")
    public ResponseEntity<ApiResponse<DiagnosticResponse>> diagnose(
            @Valid @RequestBody DiagnoseRequest request) {

        // سيتم ربطه بالـ SmartMechanicService
        // DiagnosticResponse result = mechanicService.diagnose(request.vin(), request.dtcCode());
        return ResponseEntity.ok(ApiResponse.ok("تم التشخيص بنجاح", null));
    }

    /**
     * GET /api/v1/diagnose/steps/{manualId}
     * إرجاع خطوات الإصلاح فقط بصيغة JSON نقية للموبايل
     */
    @GetMapping("/steps/{manualId}")
    @Operation(summary = "الحصول على خطوات الإصلاح")
    public ResponseEntity<ApiResponse<String>> getStepsAsJson(
            @PathVariable String manualId) {
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    /**
     * GET /api/v1/diagnose/complaints/{dtcCode}
     * تحليل شكاوى المستهلكين لكود معين
     */
    @GetMapping("/complaints/{dtcCode}")
    @Operation(summary = "تحليل الشكاوى الشائعة")
    public ResponseEntity<ApiResponse<?>> getComplaints(
            @PathVariable String dtcCode) {
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}


/**
 * ══════════════════════════════════════════════════════
 *  كتالوج السيارات — CRUD كامل
 * ══════════════════════════════════════════════════════
 */
@RestController
@RequestMapping("/cars")
@Tag(name = "كتالوج السيارات", description = "إدارة بيانات السيارات")
@SecurityRequirement(name = "Bearer Authentication")
class CarCatalogController {

    /**
     * GET /api/v1/cars?page=0&size=10&sort=manufacturer
     * قائمة السيارات مع Pagination
     */
    @GetMapping
    @Operation(summary = "قائمة جميع السيارات")
    public ResponseEntity<ApiResponse<Page<CarSummaryResponse>>> getAllCars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "manufacturer") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        // سيتم ربطه بالـ Service
        return ResponseEntity.ok(ApiResponse.ok(Page.empty()));
    }

    /**
     * GET /api/v1/cars/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "تفاصيل سيارة محددة")
    public ResponseEntity<ApiResponse<CarSummaryResponse>> getCarById(
            @PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    /**
     * GET /api/v1/cars/vin/{vin}
     * البحث برقم الشاسيه
     */
    @GetMapping("/vin/{vin}")
    @Operation(summary = "البحث برقم الشاسيه")
    public ResponseEntity<ApiResponse<CarSummaryResponse>> getCarByVin(
            @PathVariable String vin) {
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    /**
     * GET /api/v1/cars/search?q=toyota
     */
    @GetMapping("/search")
    @Operation(summary = "بحث شامل في السيارات")
    public ResponseEntity<ApiResponse<Page<CarSummaryResponse>>> searchCars(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.ok(Page.empty()));
    }

    /**
     * POST /api/v1/cars
     * إضافة سيارة جديدة
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "إضافة سيارة جديدة")
    public ResponseEntity<ApiResponse<CarSummaryResponse>> createCar(
            @Valid @RequestBody Object request) {
        return ResponseEntity.status(201).body(ApiResponse.ok("تم إضافة السيارة", null));
    }

    /**
     * PUT /api/v1/cars/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "تحديث بيانات سيارة")
    public ResponseEntity<ApiResponse<CarSummaryResponse>> updateCar(
            @PathVariable String id,
            @RequestBody Object request) {
        return ResponseEntity.ok(ApiResponse.ok("تم التحديث", null));
    }

    /**
     * DELETE /api/v1/cars/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "حذف سيارة")
    public ResponseEntity<ApiResponse<Void>> deleteCar(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok("تم الحذف", null));
    }
}


/**
 * ══════════════════════════════════════════════════════
 *  أكواد الأعطال DTC
 * ══════════════════════════════════════════════════════
 */
@RestController
@RequestMapping("/dtc")
@Tag(name = "أكواد الأعطال DTC", description = "قاعدة بيانات أكواد الأعطال التشخيصية")
@SecurityRequirement(name = "Bearer Authentication")
class DtcController {

    /**
     * GET /api/v1/dtc/{code}
     * مثال: GET /api/v1/dtc/P0300
     */
    @GetMapping("/{code}")
    @Operation(summary = "تفاصيل كود عطل محدد",
               description = "مثال: P0300 — إطلاق عشوائي في المحرك")
    public ResponseEntity<ApiResponse<FaultInfo>> getDtcCode(
            @PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    /**
     * GET /api/v1/dtc?severity=CRITICAL
     */
    @GetMapping
    @Operation(summary = "قائمة أكواد الأعطال مع تصفية")
    public ResponseEntity<ApiResponse<?>> getDtcCodes(
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    /**
     * GET /api/v1/dtc/search?q=misfire
     */
    @GetMapping("/search")
    @Operation(summary = "البحث في أكواد الأعطال")
    public ResponseEntity<ApiResponse<?>> searchDtc(@RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    /**
     * GET /api/v1/dtc/critical
     * الأكواد الحرجة فقط
     */
    @GetMapping("/critical")
    @Operation(summary = "الأكواد الحرجة التي تستوجب إيقاف السيارة")
    public ResponseEntity<ApiResponse<?>> getCriticalCodes() {
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}


/**
 * ══════════════════════════════════════════════════════
 *  المصادقة — تسجيل دخول وإنشاء حساب
 * ══════════════════════════════════════════════════════
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "المصادقة", description = "تسجيل الدخول والحصول على JWT Token")
class AuthController {

    /**
     * POST /api/v1/auth/login
     *
     * مثال الطلب:
     * { "email": "admin@mechanic.com", "password": "Admin@123" }
     *
     * مثال الرد:
     * { "accessToken": "eyJ...", "refreshToken": "eyJ...", "tokenType": "Bearer" }
     */
    @PostMapping("/login")
    @Operation(summary = "تسجيل الدخول والحصول على JWT")
    import com.mechanic.dto.response.AuthResponse;
import com.mechanic.dto.response.ApiResponse;
import com.mechanic.dto.response.CarSummaryResponse;
import com.mechanic.dto.response.DiagnosticResponse;
import com.mechanic.dto.response.FaultInfo;
public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody Object request) {
        return ResponseEntity.ok(ApiResponse.ok("تم تسجيل الدخول", null));
    }

    /**
     * POST /api/v1/auth/register
     */
    @PostMapping("/register")
    @Operation(summary = "إنشاء حساب جديد")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody Object request) {
        return ResponseEntity.status(201).body(ApiResponse.ok("تم إنشاء الحساب", null));
    }

    /**
     * POST /api/v1/auth/refresh
     * تجديد الـ Access Token باستخدام الـ Refresh Token
     */
    @PostMapping("/refresh")
    @Operation(summary = "تجديد الـ Access Token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @RequestHeader("Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
