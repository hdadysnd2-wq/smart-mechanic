package com.mechanic.dto.response;

import java.util.List;

public record DiagnosticResponse(
        VehicleInfo vehicle,
        FaultInfo fault,
        String repairStrategy,
        List<String> safetyPrerequisites,
        List<String> requiredTools,
        List<StepResponse> steps,
        int estimatedTimeMinutes,
        boolean requiresProgramming
) {}
