/*
 * Copyright 2011-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.shell.plugin.support;

import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.springframework.shell.core.AnnotatedCommand;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.shell.plugin.HelpFormatter;
import org.springframework.shell.support.logging.HandlerUtils;
import org.springframework.shell.support.util.NaturalOrderComparator;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Default Help Formatter.
 *
 * @author Jeferson Estevo
 */
@Component
public class DefaultHelpFormatter implements HelpFormatter {

    private static final Logger LOGGER = HandlerUtils.getLogger(DefaultHelpFormatter.class);

    private static final Comparator<Object> COMPARATOR = new NaturalOrderComparator<Object>();

    public String getProviderName() {
	    return "Spring Shell";
    }

    @Override
    public void displayHelpForCommands(List<AnnotatedCommand> annotatedCommands) {
	StringBuilder sb = new StringBuilder();

	// Figure out if there's a single command we can offer help for
	if (annotatedCommands.size() == 1) {
	    AnnotatedCommand annotatedCommand = annotatedCommands.get(0);
	    Assert.notNull(annotatedCommand, "AnnotatedCommand must not be null");

	    CliCommand cmd = annotatedCommand.getCliCommand();

	    for (String value : cmd.value()) {
		sb.append("Keyword:                   ").append(value).append(OsUtils.LINE_SEPARATOR);
	    }
	    sb.append("Description:               ").append(cmd.help()).append(OsUtils.LINE_SEPARATOR);

	    for (CliOption cliOption : annotatedCommand.getOptions()) {
		for (String key : cliOption.key()) {
		    if ("".equals(key)) {
			key = "** default **";
		    }
		    sb.append(" Keyword:                  ").append(key).append(OsUtils.LINE_SEPARATOR);
		}

		sb.append("   Help:                   ").append(cliOption.help())
			.append(OsUtils.LINE_SEPARATOR);
		sb.append("   Mandatory:              ").append(cliOption.mandatory())
			.append(OsUtils.LINE_SEPARATOR);
		sb.append("   Default if specified:   '").append(cliOption.specifiedDefaultValue())
			.append("'").append(OsUtils.LINE_SEPARATOR);
		sb.append("   Default if unspecified: '").append(cliOption.unspecifiedDefaultValue())
			.append("'").append(OsUtils.LINE_SEPARATOR);
		sb.append(OsUtils.LINE_SEPARATOR);
	    }
	}

	SortedSet<String> result = new TreeSet<String>(COMPARATOR);
	for (AnnotatedCommand annotatedCommand : annotatedCommands) {
	    CliCommand cmd = annotatedCommand.getCliCommand();
	    if (cmd != null) {
		for (String value : cmd.value()) {
		    if ("".equals(cmd.help())) {
			result.add("* " + value);
		    }
		    else {
			result.add("* " + value + " - " + cmd.help());
		    }
		}
	    }
	}

	for (String s : result) {
	    sb.append(s).append(OsUtils.LINE_SEPARATOR);
	}

	LOGGER.info(sb.toString());

    }

    @Override
    public void displayHelpForOption(String lastOptionKey, CliOption option) {
	StringBuilder help = new StringBuilder();
	help.append(OsUtils.LINE_SEPARATOR);
	help.append(option.mandatory() ? "required --" : "optional --");
	if ("".equals(option.help())) {
	    help.append(lastOptionKey).append(": ").append("No help available");
	}
	else {
	    help.append(lastOptionKey).append(": ").append(option.help());
	}
	if (option.specifiedDefaultValue().equals(option.unspecifiedDefaultValue())) {
	    if (option.specifiedDefaultValue().equals("__NULL__")) {
		help.append("; no default value");
	    }
	    else {
		help.append("; default: '").append(option.specifiedDefaultValue()).append("'");
	    }
	}
	else {
	    if (!"".equals(option.specifiedDefaultValue()) && !"__NULL__".equals(option.specifiedDefaultValue())) {
		help.append("; default if option present: '").append(option.specifiedDefaultValue()).append("'");
	    }
	    if (!"".equals(option.unspecifiedDefaultValue()) && !"__NULL__".equals(option.unspecifiedDefaultValue())) {
		help.append("; default if option not present: '").append(option.unspecifiedDefaultValue()).append("'");
	    }
	}
	LOGGER.info(help.toString());
    }

}