package com.hlianole.codecomplexity.constants;

import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

public final class TextAttributeKeys {

    public static final TextAttributesKey MEDIUM_COMPLEXITY_KEY = TextAttributesKey.createTextAttributesKey(
            "MEDIUM_CC_KEY",
            CodeInsightColors.WEAK_WARNING_ATTRIBUTES
    );

    public static final TextAttributesKey HIGH_COMPLEXITY_KEY = TextAttributesKey.createTextAttributesKey(
            "HIGH_CC_KEY",
            CodeInsightColors.WARNINGS_ATTRIBUTES
    );

}
