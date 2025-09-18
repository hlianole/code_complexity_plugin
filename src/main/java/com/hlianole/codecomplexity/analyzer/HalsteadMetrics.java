package com.hlianole.codecomplexity.analyzer;

import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

class HalsteadMetrics {

    private final int uniqueOperators;
    private final int uniqueOperands;
    private final int totalOperators;
    private final int totalOperands;

    HalsteadMetrics(
            int uniqueOperators, int uniqueOperands, int totalOperators, int totalOperands
    ) {
        this.uniqueOperators = uniqueOperators;
        this.uniqueOperands = uniqueOperands;
        this.totalOperators = totalOperators;
        this.totalOperands = totalOperands;
    }

    public double getVolume() {
        int vocabulary = uniqueOperators + uniqueOperands;
        int length = totalOperators + totalOperands;

        if (vocabulary == 0) return 0;
        return length * (Math.log(vocabulary) / Math.log(2));
    }

    public double getDifficulty() {
        if (uniqueOperands == 0) return 0;
        return (uniqueOperators / 2.0) * (totalOperands / (double) uniqueOperands);
    }

    public double getEffort() {
        return getDifficulty() * getVolume();
    }
}



class HalsteadCalculator extends JavaRecursiveElementVisitor {

    private final Set<String> operators = new HashSet<>();
    private final Set<String> operands = new HashSet<>();

    private int operatorCount = 0;
    private int operandCount = 0;

    @Override
    public void visitBinaryExpression(@NotNull PsiBinaryExpression expression) {
        super.visitBinaryExpression(expression);

        String operator = expression.getOperationSign().getText();

        operators.add(operator);
        operatorCount++;
    }

    @Override
    public void visitUnaryExpression(@NotNull PsiUnaryExpression expression) {
        super.visitUnaryExpression(expression);
        String operator = expression.getOperationSign().getText();

        operators.add(operator);
        operatorCount++;
    }

    @Override
    public void visitAssignmentExpression(@NotNull PsiAssignmentExpression expression) {
        super.visitAssignmentExpression(expression);
        String operator = expression.getOperationSign().getText();

        operators.add(operator);
        operatorCount++;
    }

    @Override
    public void visitMethodCallExpression(@NotNull PsiMethodCallExpression expression) {
        super.visitMethodCallExpression(expression);
        String methodName = expression.getMethodExpression().getReferenceName();
        if (methodName != null) {
            operators.add(methodName);
            operatorCount++;
        }
    }

    @Override
    public void visitIfStatement(@NotNull PsiIfStatement statement) {
        super.visitIfStatement(statement);
        operators.add("if");
        operatorCount++;
    }

    @Override
    public void visitForStatement(@NotNull PsiForStatement statement) {
        super.visitForStatement(statement);
        operators.add("for");
        operatorCount++;
    }

    @Override
    public void visitWhileStatement(@NotNull PsiWhileStatement statement) {
        super.visitWhileStatement(statement);
        operators.add("while");
        operatorCount++;
    }

    @Override
    public void visitDoWhileStatement(@NotNull PsiDoWhileStatement statement) {
        super.visitDoWhileStatement(statement);
        operators.add("do-while");
        operatorCount++;
    }

    @Override
    public void visitSwitchStatement(@NotNull PsiSwitchStatement statement) {
        super.visitSwitchStatement(statement);
        operators.add("switch");
        operatorCount++;
    }

    @Override
    public void visitReturnStatement(@NotNull PsiReturnStatement statement) {
        super.visitReturnStatement(statement);
        operators.add("return");
        operatorCount++;
    }

    @Override
    public void visitReferenceExpression(@NotNull PsiReferenceExpression expression) {
        super.visitReferenceExpression(expression);
        String name = expression.getReferenceName();
        if (name != null && !operators.contains(name)) {
            operands.add(name);
            operandCount++;
        }
    }

    @Override
    public void visitLiteralExpression(@NotNull PsiLiteralExpression expression) {
        super.visitLiteralExpression(expression);
        String value = expression.getText();
        operands.add(value);
        operandCount++;
    }

    public HalsteadMetrics getMetrics() {
        return new HalsteadMetrics(
                operators.size(), operands.size(), operatorCount, operandCount
        );
    }
}
