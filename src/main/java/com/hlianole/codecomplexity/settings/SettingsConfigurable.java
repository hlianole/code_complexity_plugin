package com.hlianole.codecomplexity.settings;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SettingsConfigurable implements Configurable {

    private JCheckBox CheckBoxCC;
    private JCheckBox CheckBoxMI;
    private JPanel mainPanel;

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Code Complexity Analyzer";
    }

    @Override
    public @Nullable JComponent createComponent() {
        mainPanel = new JPanel();
        CheckBoxCC = new JCheckBox("Show warning when high Cyclomatic Complexity");
        CheckBoxMI = new JCheckBox("Show warning when low Maintainability Index");

        mainPanel.add(CheckBoxCC);
        mainPanel.add(CheckBoxMI);

        reset();
        return mainPanel;
    }

    @Override
    public boolean isModified() {
        Settings settings = Settings.getInstance();
        return CheckBoxCC.isSelected() != settings.enableCCHighlighting ||
                CheckBoxMI.isSelected() != settings.enableMIHighlighting;
    }

    @Override
    public void apply() {
        Settings settings = Settings.getInstance();

        boolean wasCCEnabled = settings.enableCCHighlighting;
        boolean wasMIEnabled = settings.enableMIHighlighting;

        settings.enableCCHighlighting = CheckBoxCC.isSelected();
        settings.enableMIHighlighting = CheckBoxMI.isSelected();

        if (wasCCEnabled != settings.enableCCHighlighting
        || wasMIEnabled != settings.enableMIHighlighting) {

            Project[] projects = ProjectManager.getInstance().getOpenProjects();
            for (Project project : projects) {
                if (project.isOpen() && !project.isDisposed()) {
                    DaemonCodeAnalyzer daemon = DaemonCodeAnalyzer.getInstance(project);
                    if (daemon != null) {
                        daemon.restart();
                    }
                }
            }

        }
    }

    @Override
    public void reset() {
        Settings settings = Settings.getInstance();
        CheckBoxCC.setSelected(settings.enableCCHighlighting);
        CheckBoxMI.setSelected(settings.enableMIHighlighting);
    }

    @Override
    public void disposeUIResources() {
        mainPanel = null;
        CheckBoxCC = null;
        CheckBoxMI = null;
    }
}
