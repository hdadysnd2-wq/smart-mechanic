package com.mechanic.controller;

import com.mechanic.dto.request.DiagnoseRequest;
import com.mechanic.dto.response.ApiResponse;
import com.mechanic.dto.response.AuthResponse;
import com.mechanic.dto.response.CarSummaryResponse;
import com.mechanic.dto.response.DiagnosticResponse;
import com.mechanic.dto.response.FaultInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diagnose")
@Tag(name = "التشخيص", description = "تشخيص الأعطال وإرجاع خطوات الإصلاح")
@SecurityRequirement(name = "Bearer Authentication")
public class DiagnoseController {

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "تشخيص عطل السيارة")
    public ResponseEntity<ApiResponse<DiagnosticResponse>> diagnose(
            @Valid @RequestBody DiagnoseRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("تم التشخيص بنجاح", null));
    }

    @GetMapping("/steps/{manualId}")
    @Operation(summary = "الحصول على خطوات الإصلاح")
    public ResponseEntity<ApiResponse<String>> getStepsAsJson(
            @PathVariable String manualId) {
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/complaints/{dtcCode}")
    @Operation(summary = "تحليل الشكاوى الشائعة")
    public ResponseEntity<ApiResponse<?>> getComplaints(
            @PathVariable String dtcCode) {
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}


@RestController
@RequestMapping("/cars")
@Tag(name = "كتالوج السيارات", description = "إدارة بيانات السيارات")
@SecurityRequirement(name = "Bearer Authentication")
class CarCatalogController {

    @GetMapping
    @Operation(summary = "قائمة جميع السيارات")
    public ResponseEntity<ApiResponse<Page<CarSummaryResponse>>> getAllCars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "manufacturer") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(ApiResponse.ok(Page.empty()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "تفاصيل سيارة محددة")
    public ResponseEntity<ApiResponse<CarSummaryResponse>> getCarById(
            @PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/vin/{vin}")
    @Operation(summary = "البحث برقم الشاسيه")
    public ResponseEntity<ApiResponse<CarSummaryResponse>> getCarByVin(
            @PathVariable String vin) {
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/search")
    @Operation(summary = "بحث شامل في السيارات")
    public ResponseEntity<ApiResponse<Page<CarSummaryResponse>>> searchCars(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.ok(Page.empty()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "إضافة سيارة جديدة")
    public ResponseEntity<ApiResponse<CarSummaryResponse>> createCar(
            @Valid @RequestBody Object request) {
        return ResponseEntity.status(201).body(ApiResponse.ok("تم إضافة السيارة", null));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "تحديث بيانات سيارة")
    public ResponseEntity<ApiResponse<CarSummaryResponse>> updateCar(
            @PathVariable String id,
            @RequestBody Object request) {
        return ResponseEntity.ok(ApiResponse.ok("تم التحديث", null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "حذف سيارة")
    public ResponseEntity<ApiResponse<Void>> deleteCar(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok("تم الحذف", null));
    }
}


@RestController
@RequestMapping("/dtc")
@Tag(name = "أكواد الأعطال DTC", description = "قاعدة بيانات أكواد الأعطال التشخيصية")
@SecurityRequirement(name = "Bearer Authentication")
class DtcController {

    @GetMapping("/{code}")
    @Operation(summary = "تفاصيل كود عطل محدد")
    public ResponseEntity<ApiResponse<FaultInfo>> getDtcCode(
            @PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping
    @Operation(summary = "قائمة أكواد الأعطال مع تصفية")
    public ResponseEntity<ApiResponse<?>> getDtcCodes(
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/search")
    @Operation(summary = "البحث في أكواد الأعطال")
    public ResponseEntity<ApiResponse<?>> searchDtc(@RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/critical")
    @Operation(summary = "الأكواد الحرجة")
    public ResponseEntity<ApiResponse<?>> getCriticalCodes() {
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}


@RestController
@RequestMapping("/auth")
@Tag(name = "المصادقة", description = "تسجيل الدخول والحصول على JWT Token")
class AuthController {

    @PostMapping("/login")
    @Operation(summary = "تسجيل الدخول والحصول على JWT")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody Object request) {
        return ResponseEntity.ok(ApiResponse.ok("تم تسجيل الدخول", null));
    }

    @PostMapping("/register")
    @Operation(summary = "إنشاء حساب جديد")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody Object request) {
        return ResponseEntity.status(201).body(ApiResponse.ok("تم إنشاء الحساب", null));
    }

    @PostMapping("/refresh")
    @Operation(summary = "تجديد الـ Access Token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @RequestHeader("Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
