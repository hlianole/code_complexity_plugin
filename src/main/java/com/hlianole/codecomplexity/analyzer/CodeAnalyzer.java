package com.hlianole.codecomplexity.analyzer;

import com.hlianole.codecomplexity.model.AnalysisActionResult;
import com.hlianole.codecomplexity.model.AnalysisStaticResult;
import com.hlianole.codecomplexity.settings.Settings;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CodeAnalyzer {

    public @NotNull AnalysisActionResult analyzeAction(@NotNull PsiMethod method) {
        PsiCodeBlock body = method.getBody();
        if (body == null) {
            return AnalysisActionResult.emptyBody();
        }

        int cyclomaticComplexity = getCyclomaticComplexity(method);
        int physicalLinesCount = countPhysicalLines(body);
        int logicalLinesCount = countLogicalLines(body);

        double maintainabilityIndex = countMaintainabilityIndex(cyclomaticComplexity, logicalLinesCount, body);

        return new AnalysisActionResult(cyclomaticComplexity, maintainabilityIndex, physicalLinesCount, logicalLinesCount);
    }

    public @NotNull AnalysisStaticResult analyzeStatic(@NotNull PsiMethod method) {
        PsiCodeBlock body = method.getBody();
        if (body == null) {
            return AnalysisStaticResult.emptyBody();
        }

        Settings settings = Settings.getInstance();

        int cyclomaticComplexity = 1;
        double maintainabilityIndex = 100.0;

        if (settings.enableCCHighlighting || settings.enableMIHighlighting) {
            cyclomaticComplexity = getCyclomaticComplexity(method);
            if (settings.enableCCHighlighting) {
                int logicalLinesCount = countLogicalLines(body);
                maintainabilityIndex = countMaintainabilityIndex(cyclomaticComplexity, logicalLinesCount, body);
            }
        }

        return new AnalysisStaticResult(cyclomaticComplexity, maintainabilityIndex);
    }

    private static int getCyclomaticComplexity(@NotNull PsiMethod method) {
        ComplexityVisitor visitor = new ComplexityVisitor();
        method.accept(visitor);
        return visitor.getComplexity();
    }

    private static int countLogicalLines(@NotNull PsiCodeBlock block) {
        LogicalLinesCounter logicalLinesCounter = new LogicalLinesCounter();
        block.accept(logicalLinesCounter);
        return logicalLinesCounter.getLinesCount();
    }


    private static int countPhysicalLines(@NotNull PsiCodeBlock block) {
        return (int) Arrays.stream(block.getText().split("\n")).count();
    }


    private double countMaintainabilityIndex(
            int cyclomaticComplexity, int linesCount, @NotNull PsiCodeBlock block
    ) {
        int nonZeroLinesCount = linesCount == 0 ? 1 : linesCount;

        HalsteadMetrics halstead = calculateHalsteadMetrics(block);
        double halsteadVolume = halstead.getVolume();

        double maintainabilityIndex = maintainabilityFormula(
                cyclomaticComplexity, nonZeroLinesCount, halsteadVolume
        );

        maintainabilityIndex = Math.max(0, maintainabilityIndex * 100 / 171);
        return Math.round(maintainabilityIndex * 100.0) / 100.0;
    }


    private double maintainabilityFormula(int cc, int loc, double hm) {
        double hvLog = hm > 0 ? Math.log(hm) : 0;
        return 171 - 5.2 * hvLog - 0.23 * cc - 16.2 * Math.log(loc);
    }


    private HalsteadMetrics calculateHalsteadMetrics(@NotNull PsiCodeBlock block) {
        HalsteadCalculator halsteadCalculator = new HalsteadCalculator();
        block.accept(halsteadCalculator);
        return halsteadCalculator.getMetrics();
    }
}
