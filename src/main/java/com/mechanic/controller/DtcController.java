package com.mechanic.controller;

import com.mechanic.dto.response.ApiResponse;
import com.mechanic.dto.response.FaultInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dtc")
public class DtcController {

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<FaultInfo>> getDtcCode(@PathVariable String code) {
        var fault = new FaultInfo(code,
                "خلل في النظام",
                "وصف تفصيلي للكود " + code,
                "Engine", "MEDIUM", "Engine System",
                List.of("سبب محتمل 1", "سبب محتمل 2"),
                List.of("مكون 1", "مكون 2"),
                150.0);
        return ResponseEntity.ok(ApiResponse.ok(fault));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<String>>> getDtcCodes() {
        return ResponseEntity.ok(ApiResponse.ok(List.of("P0300", "P0171", "P0420", "U0100")));
    }
}
