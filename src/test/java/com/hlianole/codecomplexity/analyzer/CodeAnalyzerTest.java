package com.hlianole.codecomplexity.analyzer;

import com.hlianole.codecomplexity.BasePluginTest;
import com.hlianole.codecomplexity.model.AnalysisActionResult;
import com.intellij.psi.PsiMethod;

public class CodeAnalyzerTest extends BasePluginTest {

    private CodeAnalyzer codeAnalyzer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        codeAnalyzer = new CodeAnalyzer();
    }

    public void testShouldHaveLowCCAndHighMI() {
        PsiMethod method = createMethodFromString("""
            void greet() {
                System.out.println("Hello");
            }
            """);

        AnalysisActionResult result = codeAnalyzer.analyzeAction(method);

        assertEquals(1, result.cyclomaticComplexity());
        assertTrue(result.maintainabilityIndex() > 90);
        assertEquals(3, result.physicalLinesOfCode());
        assertEquals(1, result.logicalLinesOfCode());
    }

    public void testShouldHaveMediumCCAndMediumMI() {
        PsiMethod method = createMethodFromString("""
            void processData(int x) {
                if (x > 0) {
                    for (int i = 0; i < x; i++) {
                        if (i % 2 == 0) {
                            System.out.println("Even");
                        }
                        if (i % 2 != 0) {
                            System.out.println("Odd");
                        }
                    }
                } else if (x < 0) {
                    System.out.println("Negative");
                }
                if (x == 0) {
                    System.out.println("Zero");
                }
                if (x > 100) {
                    System.out.println("More than 100");
                }
                if (x < -100) {
                    System.out.println("Less than -100");
                }
                int y = 5;
                while (y < 10) {
                    y++;
                    if (y % 2 == 0) {
                        System.out.println("Even");
                    }
                }
            }
            """);

        AnalysisActionResult result = codeAnalyzer.analyzeAction(method);

        assertEquals(11, result.cyclomaticComplexity());
        assertTrue(result.maintainabilityIndex() < 65);
        assertEquals(23, result.logicalLinesOfCode());
        assertEquals(30, result.physicalLinesOfCode());
    }

    public void testShouldHaveHighCCAndLowMI() {
        PsiMethod method = createMethodFromString("""
            void processData(int x) {
                if (x > 0) {
                    for (int i = 0; i < x; i++) {
                        if (i % 2 == 0) {
                            System.out.println("Even");
                            int z = 5;
                            while (z < 10) {
                                z++;
                                if (z % 2 == 0) {
                                    System.out.println("Even");
                                    int a = 100;
                                    for (int j = a; j < 110; j++) {
                                        if (j % 2 == 0) {
                                            System.out.println("Even");
                                        }
                                        if (j % 2 != 0) {
                                            System.out.println("Odd");
                                       }
                                    }
                                }
                                if (z % 2 != 0) {
                                    System.out.println("Odd");
                                }
                            }
                        }
                        if (i % 2 != 0) {
                            System.out.println("Odd");
                        }
                    }
                } else if (x < 0) {
                    System.out.println("Negative");
                }
                if (x == 0) {
                    System.out.println("Zero");
                    int temp = 10;
                    if (temp % 2 == 0 && temp % 5 == 0) {
                        System.out.println("Simulate some logic...");
                    }
                }
                if (x > 100) {
                    System.out.println("More than 100");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted");
                    }
                }

                int var1 = 5;
                int var2 = 10;
                int var3 = 7;
                int var4 = 12;

                int var5 = (var1 - var3) / var2 * var4;

                System.out.println("Increasing LOC");
                System.out.println("Increasing LOC");
                System.out.println("Increasing LOC");
                System.out.println("Increasing LOC");
                System.out.println("Increasing LOC");
                System.out.println("Increasing LOC");
                System.out.println("Increasing LOC");
                System.out.println("Increasing LOC");
                System.out.println("Increasing LOC");
                System.out.println("Increasing LOC");
                System.out.println("Increasing LOC");
                System.out.println("Increasing LOC");
                System.out.println("Increasing LOC");
                System.out.println("Increasing LOC");

                if (x < -100) {
                    System.out.println("Less than -100");
                }
                int y = 5;
                while (y < 10) {
                    y++;
                    if (y % 2 == 0) {
                        System.out.println("Even");
                    }
                }
            }
           """);

        AnalysisActionResult result = codeAnalyzer.analyzeAction(method);

        assertEquals(20, result.cyclomaticComplexity());
        assertTrue(result.maintainabilityIndex() < 40);
        assertEquals(72, result.logicalLinesOfCode());
        assertEquals(81, result.physicalLinesOfCode());
    }
}
