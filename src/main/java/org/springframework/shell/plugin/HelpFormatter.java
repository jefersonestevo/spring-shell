package org.springframework.shell.plugin;

import java.util.List;

import org.springframework.shell.core.AnnotatedCommand;
import org.springframework.shell.core.annotation.CliOption;

/**
 * Help formatter.
 * Plugins should implement this interface to customize the help format.
 * <code>getOrder</code> indicate the priority, higher values can be interpreted as lower priority
 *
 * @author Jeferson Estevo
 *
 */
public interface HelpFormatter extends NamedProvider {

    /**
     * Format and show help for this command on the console
     *
     * @param annotatedCommands Annotated commands found
     */
    void displayHelpForCommands(List<AnnotatedCommand> annotatedCommands);

    /**
     * Format and show help for this option on the console
     *
     * @param lastOptionKey
     * @param option
     */
    void displayHelpForOption(String lastOptionKey, CliOption option);

}
