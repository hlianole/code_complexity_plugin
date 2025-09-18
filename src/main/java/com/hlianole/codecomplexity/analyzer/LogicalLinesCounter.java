package com.hlianole.codecomplexity.analyzer;

import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import static com.hlianole.codecomplexity.constants.ComplexityConstants.DEFAULT_LOGICAL_LINES_COUNT;

class LogicalLinesCounter extends JavaRecursiveElementVisitor {

    private int count = DEFAULT_LOGICAL_LINES_COUNT;

    @Override
    public void visitStatement(@NotNull PsiStatement statement) {
        super.visitStatement(statement);

        if (!(statement instanceof PsiBlockStatement)
                && !(statement instanceof PsiEmptyStatement)
        ) {
            count++;
        }
    }

    @Override
    public void visitDeclarationStatement(@NotNull PsiDeclarationStatement statement) {
        super.visitDeclarationStatement(statement);

        PsiElement[] declaredElements = statement.getDeclaredElements();
        count += declaredElements.length;
    }

    @Override
    public void visitLambdaExpression(@NotNull PsiLambdaExpression expression) {
        super.visitLambdaExpression(expression);
        count++;
    }

    public int getLinesCount() {
        return count;
    }
}
