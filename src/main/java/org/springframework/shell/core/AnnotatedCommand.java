package org.springframework.shell.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;

public class AnnotatedCommand {
    private CliCommand cliCommand;
    private List<CliOption> options = new ArrayList<CliOption>();

    public AnnotatedCommand(CliCommand cliCommand) {
        this.cliCommand = cliCommand;
    }

    public CliCommand getCliCommand() {
        return cliCommand;
    }

    public void setCliCommand(CliCommand cliCommand) {
        this.cliCommand = cliCommand;
    }

    public List<CliOption> getOptions() {
        return options;
    }

    public void addOption(CliOption option) {
        this.options.add(option);
    }
}
