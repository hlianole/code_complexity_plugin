package com.hlianole.codecomplexity.action;

import com.hlianole.codecomplexity.Bundle;
import com.hlianole.codecomplexity.analyzer.CodeAnalyzer;
import com.hlianole.codecomplexity.model.AnalysisActionResult;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

public class AnalyzeMethodAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        PsiElement element = anActionEvent.getData(CommonDataKeys.PSI_ELEMENT);
        if (element instanceof PsiMethod method) {
            CodeAnalyzer analyzer = new CodeAnalyzer();
            AnalysisActionResult result = analyzer.analyzeAction(method);

            String message = Bundle.get("analyze.message",
                    result.cyclomaticComplexity(),
                    result.maintainabilityIndex(),
                    result.physicalLinesOfCode(),
                    result.logicalLinesOfCode()
            );

            Messages.showMessageDialog(
                    anActionEvent.getProject(),
                    message,
                    Bundle.get("analyze.result"),
                    Messages.getInformationIcon()
            );
        }
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void update(@NotNull AnActionEvent anActionEvent) {
        PsiElement psiElement = anActionEvent.getData(CommonDataKeys.PSI_ELEMENT);
        boolean enabled = psiElement instanceof PsiMethod;
        anActionEvent.getPresentation().setEnabled(enabled);
        anActionEvent.getPresentation().setVisible(true);
    }
}
