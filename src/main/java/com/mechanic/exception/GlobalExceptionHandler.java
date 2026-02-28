package com.mechanic.exception;

import com.mechanic.dto.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * ══════════════════════════════════════════════════════
 *  معالج الأخطاء العام — يُوحّد شكل رسائل الخطأ
 *  كل خطأ يُعاد بنفس هيكل ApiResponse
 * ══════════════════════════════════════════════════════
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * أخطاء التحقق من البيانات (Validation)
     * مثال: VIN قصير، DTC code بصيغة خاطئة
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("بيانات غير صالحة — تحقق من المدخلات", "VALIDATION_ERROR"));
    }

    /**
     * بيانات غير موجودة في قاعدة البيانات
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), "NOT_FOUND"));
    }

    /**
     * رقم شاسيه مكرر
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicate(DuplicateResourceException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage(), "DUPLICATE_RESOURCE"));
    }

    /**
     * بيانات دخول خاطئة
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("البريد الإلكتروني أو كلمة المرور غير صحيحة", "INVALID_CREDENTIALS"));
    }

    /**
     * توكن منتهي الصلاحية
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<Void>> handleExpiredJwt(ExpiredJwtException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("الجلسة منتهية — يرجى تسجيل الدخول مجدداً", "TOKEN_EXPIRED"));
    }

    /**
     * توكن غير صالح
     */
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiResponse<Void>> handleMalformedJwt(MalformedJwtException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("رمز المصادقة غير صالح", "INVALID_TOKEN"));
    }

    /**
     * محاولة الوصول لمورد غير مسموح
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("ليس لديك صلاحية للوصول لهذا المورد", "ACCESS_DENIED"));
    }

    /**
     * أي خطأ غير متوقع
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("حدث خطأ داخلي في الخادم", "INTERNAL_ERROR"));
    }
}


// ── Custom Exceptions ──────────────────────────────────────────────────────

class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String id) {
        super("%s غير موجود: %s".formatted(resource, id));
    }
}

class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}

class InvalidVinException extends RuntimeException {
    public InvalidVinException(String reason) {
        super("رقم الشاسيه غير صالح: " + reason);
    }
}
