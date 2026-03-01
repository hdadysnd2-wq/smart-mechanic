package com.mechanic.controller;

import com.mechanic.dto.request.DiagnoseRequest;
import com.mechanic.dto.response.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diagnose")
public class DiagnoseController {

    @PostMapping
    public ResponseEntity<ApiResponse<DiagnosticResponse>> diagnose(
            @Valid @RequestBody DiagnoseRequest request) {

        // نموذج بيانات للتجربة
        var vehicle = new VehicleInfo(request.vin(), "Toyota Camry 2022",
                "Toyota", "Camry", 2022, "Gasoline", "ISO 15765-4");

        var fault = new FaultInfo(request.dtcCode(),
                "خلل في جهاز الاشتعال",
                "يشير كود " + request.dtcCode() + " إلى خلل في نظام الإشعال",
                "Engine", "HIGH", "Ignition System",
                List.of("بواجي تالفة", "سلك بواجي معطوب", "ضغط منخفض"),
                List.of("البواجي", "الكويلات", "ضغط الوقود"),
                250.0);

        var steps = List.of(
                new StepResponse(1, "فحص البواجي", "قم بفحص البواجي بصريًا",
                        null, null, List.of("مفتاح بواجي"), "تأكد من إيقاف المحرك",
                        null, null, 10, false),
                new StepResponse(2, "قياس المقاومة", "قس مقاومة البواجي",
                        null, null, List.of("مولتيميتر"),
                        null, null,
                        new MeasurementResponse("مقاومة", "Ω", 0.5, 2.0, 1.0, "مولتيميتر"),
                        15, true)
        );

        var result = new DiagnosticResponse(vehicle, fault,
                "استبدال البواجي",
                List.of("إيقاف المحرك", "انتظر حتى يبرد"),
                List.of("مفتاح بواجي", "مولتيميتر"),
                steps, 45, false);

        return ResponseEntity.ok(ApiResponse.ok("تم التشخيص بنجاح", result));
    }
}
