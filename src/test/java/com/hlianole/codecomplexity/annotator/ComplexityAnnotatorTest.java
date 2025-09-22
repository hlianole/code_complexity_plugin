package com.hlianole.codecomplexity.annotator;

import com.hlianole.codecomplexity.BasePluginTest;
import com.hlianole.codecomplexity.settings.Settings;
import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static com.hlianole.codecomplexity.constants.TextAttributeKeys.HIGH_COMPLEXITY_KEY;
import static com.hlianole.codecomplexity.constants.TextAttributeKeys.MEDIUM_COMPLEXITY_KEY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ComplexityAnnotatorTest extends BasePluginTest {

    private ComplexityAnnotator annotator;

    @Mock
    private AnnotationHolder annotationHolder;

    @Mock
    private AnnotationBuilder annotationBuilder;

    private List<AnnotationInfo> capturedAnnotations;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.openMocks(this);
        capturedAnnotations = new ArrayList<>();

        Settings mockSettings = mock(Settings.class);
        mockSettings.enableCCHighlighting = true;
        mockSettings.enableMIHighlighting = false;

        annotator = new ComplexityAnnotator(mockSettings);

        setupMockAnnotationHolder();
    }

    private void setupMockAnnotationHolder() {
        when(annotationHolder.newAnnotation(any(HighlightSeverity.class), anyString()))
                .thenAnswer(invocation -> {
                    HighlightSeverity severity = invocation.getArgument(0);
                    String message = invocation.getArgument(1);

                    AnnotationInfo annotationInfo = new AnnotationInfo(severity, message);
                    capturedAnnotations.add(annotationInfo);

                    when(annotationBuilder.range(any(PsiElement.class)))
                            .thenAnswer(rangeInvocation -> annotationBuilder);

                    when(annotationBuilder.textAttributes(any(TextAttributesKey.class)))
                            .thenAnswer(attributesInvocation -> {
                                annotationInfo.setTextAttributes(attributesInvocation.getArgument(0));
                                return annotationBuilder;
                            });

                    doNothing().when(annotationBuilder).create();

                    return annotationBuilder;
                });
    }

    public void testShouldNotHighlightMethod() {
        PsiMethod method = createMethodFromString("""
            void processData() {
                for (int i = 0; i < 10; i++) {
                    if (i % 2 == 0) {
                        System.out.println("Even");
                    } else {
                        System.out.println("Odd");
                    }
                }
            }
            """);

        annotator.annotate(method, annotationHolder);

        assertEquals(0, capturedAnnotations.size());

        capturedAnnotations.clear();
    }

    public void testShouldHighlightMediumComplexityMethod() {
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

        annotator.annotate(method, annotationHolder);

        assertFalse (capturedAnnotations.isEmpty());
        AnnotationInfo annotation = capturedAnnotations.get(0);
        assertEquals(HighlightSeverity.WARNING, annotation.getSeverity());
        assertEquals(MEDIUM_COMPLEXITY_KEY, annotation.getTextAttributes());

        capturedAnnotations.clear();
    }


    public void testShouldHighlightHighComplexityMethod() {
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
                if (x < -100) {
                    System.out.println("Less than -100");
                }
                int y = 5;
                while (y < 10) {
                    y++;
                    if (y % 2 == 0) {
                        System.out.println("Even");
                    }
                    if (y % 2 != 0) {
                        System.out.println("Odd");
                    }
                }
            }
           """);

        annotator.annotate(method, annotationHolder);

        assertFalse(capturedAnnotations.isEmpty());
        AnnotationInfo annotation = capturedAnnotations.get(0);
        assertEquals(HighlightSeverity.WARNING, annotation.getSeverity());
        assertEquals(HIGH_COMPLEXITY_KEY, annotation.getTextAttributes());

        capturedAnnotations.clear();
    }


    private static class AnnotationInfo {
        private final HighlightSeverity severity;
        private TextAttributesKey textAttributes;

        public AnnotationInfo(HighlightSeverity severity, String message) {
            this.severity = severity;
        }

        public HighlightSeverity getSeverity() { return severity; }
        public TextAttributesKey getTextAttributes() { return textAttributes; }

        public void setTextAttributes(TextAttributesKey textAttributes) { this.textAttributes = textAttributes; }
    }

}
