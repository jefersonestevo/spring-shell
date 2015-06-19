package org.springframework.shell;

import java.io.IOException;

public interface ShellCommandLineOptions {

    CommandLine parseCommandLine(String[] args) throws IOException;

}
