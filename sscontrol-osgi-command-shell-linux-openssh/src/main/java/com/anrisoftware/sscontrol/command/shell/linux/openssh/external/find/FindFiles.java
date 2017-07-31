/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-command-shell-linux-openssh.
 *
 * sscontrol-osgi-command-shell-linux-openssh is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-command-shell-linux-openssh is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-command-shell-linux-openssh. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.command.shell.linux.openssh.external.find;

import java.util.List;

import com.anrisoftware.sscontrol.types.app.external.AppException;

/**
 * Command to find files.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface FindFiles {

    /**
     * A collection of file endings to search for, for example
     *
     * <pre>
     * findFiles suffix: ['yaml', 'yml', 'json']
     * </pre>
     */
    static final String SUFFIX_ARG = "suffix";

    /**
     * A collection of patterns to search for, for example
     *
     * <pre>
     * findFiles suffix: ['\\*.yaml', '\\*.yml', '\\*.json']
     * </pre>
     */
    static final String PATTERNS_ARG = "patterns";

    /**
     * Finds the files.
     *
     * @throws AppException
     */
    List<String> call() throws AppException;

}
