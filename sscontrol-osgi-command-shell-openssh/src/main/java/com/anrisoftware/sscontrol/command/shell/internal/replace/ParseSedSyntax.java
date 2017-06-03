/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-command-shell-openssh.
 *
 * sscontrol-osgi-command-shell-openssh is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-command-shell-openssh is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-command-shell-openssh. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.command.shell.internal.replace;

import static java.util.regex.Pattern.quote;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ParseSedSyntax {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ParseSedSyntaxFactory {

        ParseSedSyntax create(@Assisted String string);

    }

    private final String string;

    private String search;

    private String replace;

    private boolean substitute;

    private char separator;

    @Inject
    ParseSedSyntax(@Assisted String string) {
        this.string = string;
        this.substitute = false;
        this.separator = '/';
    }

    public ParseSedSyntax parse() {
        int length = string.length();
        int current = 0;
        if (length > 0) {
            char c = string.charAt(current);
            switch (c) {
            case 's':
                substitute = true;
                current++;
                break;
            }
            separator = string.charAt(current);
        }
        String[] split = split(string, "" + separator, "\\");
        this.search = split[current];
        this.replace = split[current + 1];
        return this;
    }

    public String getSearch() {
        return search;
    }

    public String getReplace() {
        return replace;
    }

    public boolean isSubstitute() {
        return substitute;
    }

    private String[] split(String string, String sep, String escape) {
        String regex = "(?<!" + quote(escape) + ")" + quote(sep);
        return string.split(regex);
    }
}
