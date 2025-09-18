package com.hlianole.codecomplexity;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

import java.util.ResourceBundle;

public class Bundle {
    @NonNls
    private static final String BUNDLE_NAME = "messages.MyBundle";
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    public static String get(@PropertyKey(resourceBundle = BUNDLE_NAME) String key, Object... params) {
        return java.text.MessageFormat.format(BUNDLE.getString(key), params);
    }
}
