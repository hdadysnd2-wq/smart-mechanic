package com.mechanic.dto.response;

import java.util.List;

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
