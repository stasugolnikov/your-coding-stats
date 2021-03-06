package com.itmo.software.dev.tools.plugin.ui;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.itmo.software.dev.tools.plugin.CodingCounterService;
import com.itmo.software.dev.tools.plugin.stats.Period;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.Map;

public class StatsWindowFactory implements com.intellij.openapi.wm.ToolWindowFactory, DumbAware {
    private final Map<Period, DefaultTableModel> tables = new HashMap<>();

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        SimpleToolWindowPanel toolWindowPanel = new SimpleToolWindowPanel(true);
        toolWindowPanel.setContent(createContent(project));
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(toolWindowPanel, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    @NotNull
    private JComponent createContent(@NotNull Project project) {
        CodingCounterWindow codingCounterWindow = new CodingCounterWindow();
        JComponent windowContent = new JBScrollPane(
                codingCounterWindow,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        codingCounterWindow.setListener(() -> {
            CodingCounterService.getInstance().resetStats();
        });

        project.getMessageBus().connect().subscribe(CodingCounterService.STATS_CHANGED, stats -> {
            ApplicationManager.getApplication().invokeLater(() -> codingCounterWindow.updateData(stats));
        });
        codingCounterWindow.updateData(CodingCounterService.getInstance().getStats());
        return windowContent;
    }

    @Override
    public void init(@NotNull ToolWindow window) {
        CodingCounterService.getInstance();
    }
}
