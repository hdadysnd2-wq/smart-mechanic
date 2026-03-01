package com.mechanic.dto.response;
import java.util.List;
public record StepResponse(int stepNumber, String title, String description, String imageUrl, String videoUrl, List<String> toolsUsed, String warningNote, String torqueSpec, MeasurementResponse measurement, int estimatedMinutes, boolean isVerificationStep) {}
