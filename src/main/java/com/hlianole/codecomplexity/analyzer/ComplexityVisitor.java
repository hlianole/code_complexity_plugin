package com.hlianole.codecomplexity.analyzer;

import com.intellij.psi.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import static com.hlianole.codecomplexity.constants.ComplexityConstants.DEFAULT_COMPLEXITY;

@Getter
class ComplexityVisitor extends JavaRecursiveElementWalkingVisitor {

    private int complexity = DEFAULT_COMPLEXITY;

    @Override
    public void visitIfStatement(@NotNull PsiIfStatement statement) {
        complexity++;
        super.visitIfStatement(statement);
    }

    @Override
    public void visitWhileStatement(@NotNull PsiWhileStatement statement) {
        complexity++;
        super.visitWhileStatement(statement);
    }

    @Override
    public void visitDoWhileStatement(@NotNull PsiDoWhileStatement statement) {
        complexity++;
        super.visitDoWhileStatement(statement);
    }

    @Override
    public void visitForStatement(@NotNull PsiForStatement statement) {
        complexity++;
        super.visitForStatement(statement);
    }

    @Override
    public void visitForeachStatement(@NotNull PsiForeachStatement statement) {
        complexity++;
        super.visitForeachStatement(statement);
    }

    @Override
    public void visitSwitchStatement(@NotNull PsiSwitchStatement statement) {
        complexity++;
        super.visitSwitchStatement(statement);
    }

    @Override
    public void visitTryStatement(@NotNull PsiTryStatement statement) {
        complexity++;
        super.visitTryStatement(statement);
    }

    @Override
    public void visitConditionalExpression(@NotNull PsiConditionalExpression expression) {
        complexity++;
        super.visitConditionalExpression(expression);
    }

    @Override
    public void visitBinaryExpression(@NotNull PsiBinaryExpression expression) {
        if (
                expression.getOperationTokenType() == JavaTokenType.ANDAND ||
                        expression.getOperationTokenType() == JavaTokenType.OROR
        ) {
            complexity++;
        }
        super.visitBinaryExpression(expression);
    }

}
