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
