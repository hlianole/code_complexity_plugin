package com.hlianole.codecomplexity.model;

import org.jetbrains.annotations.NotNull;

public record AnalysisStaticResult (
        int cyclomaticComplexity,
        double maintainabilityIndex
) {
    public static @NotNull AnalysisStaticResult emptyBody() {
        return new AnalysisStaticResult(1, 100.0);
    }
}
