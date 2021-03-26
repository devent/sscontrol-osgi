/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.command.shell.internal.replace;

import static java.util.regex.Pattern.quote;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

/**
 *
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class ParseSedSyntax {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
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
