package com.hlianole.codecomplexity;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;

public abstract class BasePluginTest extends BasePlatformTestCase {

    protected final PsiMethod createMethodFromString(String methodText) {
        String fullCode = """
        class Test {
            %s
        }
        """.formatted(methodText.trim());

        myFixture.configureByText("Test.java", fullCode);
        PsiFile file = myFixture.getFile();

        if (file instanceof PsiJavaFile javaFile) {
            PsiClass clazz = javaFile.getClasses()[0];
            return clazz.getMethods()[0];
        }

        throw new IllegalStateException("Not a Java file");
    }
}
