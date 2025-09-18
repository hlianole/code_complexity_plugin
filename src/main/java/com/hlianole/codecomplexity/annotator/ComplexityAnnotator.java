package com.hlianole.codecomplexity.annotator;

import com.hlianole.codecomplexity.Bundle;
import com.hlianole.codecomplexity.analyzer.CodeAnalyzer;
import com.hlianole.codecomplexity.model.AnalysisStaticResult;
import com.hlianole.codecomplexity.settings.Settings;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.hlianole.codecomplexity.constants.ComplexityConstants.*;
import static com.hlianole.codecomplexity.constants.TextAttributeKeys.HIGH_COMPLEXITY_KEY;
import static com.hlianole.codecomplexity.constants.TextAttributeKeys.MEDIUM_COMPLEXITY_KEY;

public class ComplexityAnnotator implements Annotator {

    private final CodeAnalyzer analyzer = new CodeAnalyzer();

    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {

        if (psiElement instanceof PsiMethod psiMethod) {

            if (psiMethod.getBody() == null) {
                return;
            }

            AnalysisStaticResult analysisResult = analyzer.analyseStatic(psiMethod);
            Settings settings = Settings.getInstance();

            if (settings.enableCCHighlighting) {
                annotateCC(annotationHolder, psiMethod, analysisResult);
            }

            if (settings.enableMIHighlighting) {
                annotateMI(annotationHolder, psiMethod, analysisResult);
            }
        }
    }

    private static void annotateMI(
            @NotNull AnnotationHolder annotationHolder, PsiMethod psiMethod, AnalysisStaticResult analysisActionResult
    ) {
        if (analysisActionResult.maintainabilityIndex() < LOW_MI) {
            annotateHighComplexity(
                    annotationHolder, psiMethod, Bundle.get("annotator.warning.mi.low")
            );
        } else if (analysisActionResult.maintainabilityIndex() < MEDIUM_MI) {
            annotateMediumComplexity(
                    annotationHolder, psiMethod, Bundle.get("annotator.warning.mi.medium")
            );
        }
    }

    private static void annotateCC(
            @NotNull AnnotationHolder annotationHolder, PsiMethod psiMethod, AnalysisStaticResult analysisActionResult
    ) {
        if (analysisActionResult.cyclomaticComplexity() > HIGH_CC) {
            annotateHighComplexity(
                    annotationHolder, psiMethod, Bundle.get("annotator.warning.cc.high")
            );
        } else if (analysisActionResult.cyclomaticComplexity() > MEDIUM_CC) {
            annotateMediumComplexity(
                    annotationHolder, psiMethod, Bundle.get("annotator.warning.cc.medium")
            );
        }
    }

    private static void annotateHighComplexity(
            @NotNull AnnotationHolder annotationHolder, @NotNull PsiMethod psiMethod, String message
    ) {
        annotationHolder
                .newAnnotation(
                        HighlightSeverity.WARNING,
                        message
                ).range(Objects.requireNonNull(psiMethod.getNameIdentifier()))
                .textAttributes(HIGH_COMPLEXITY_KEY)
                .create();
    }

    private static void annotateMediumComplexity(
            @NotNull AnnotationHolder annotationHolder, @NotNull PsiMethod psiMethod, String message
    ) {
        annotationHolder
                .newAnnotation(
                        HighlightSeverity.WARNING,
                        message
                ).range(Objects.requireNonNull(psiMethod.getNameIdentifier()))
                .textAttributes(MEDIUM_COMPLEXITY_KEY)
                .create();
    }
}
