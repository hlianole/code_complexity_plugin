package com.hlianole.codecomplexity.model;

import org.jetbrains.annotations.NotNull;

public record AnalysisActionResult(
    int cyclomaticComplexity,
    double maintainabilityIndex,
    int physicalLinesOfCode,
    int logicalLinesOfCode
) {
    public static @NotNull AnalysisActionResult emptyBody() {
        return new AnalysisActionResult(1, 100.0, 0, 0);
    }
}
