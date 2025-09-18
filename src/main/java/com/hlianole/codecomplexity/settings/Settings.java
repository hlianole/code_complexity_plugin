package com.hlianole.codecomplexity.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "CodeComplexitySettings",
        storages = @Storage("code_complexity_settings.xml")
)
public class Settings implements PersistentStateComponent<Settings> {
    public boolean enableCCHighlighting = true;
    public boolean enableMIHighlighting = false;

    private static final Settings INSTANCE = new Settings();

    public static Settings getInstance() {
        return INSTANCE;
    }

    @Override
    public @Nullable Settings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull Settings settings) {
        XmlSerializerUtil.copyBean(settings, INSTANCE);
    }
}
