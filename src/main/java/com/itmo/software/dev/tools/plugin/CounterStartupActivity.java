package com.itmo.software.dev.tools.plugin;


import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

public class CounterStartupActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        CodingCounterService.getInstance();
    }
}
