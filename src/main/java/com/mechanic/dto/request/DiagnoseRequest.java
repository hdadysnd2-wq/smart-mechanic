package com.mechanic.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DiagnoseRequest(
        @NotBlank String vin,
        @NotBlank String dtcCode,
        String additionalInfo
) {}
