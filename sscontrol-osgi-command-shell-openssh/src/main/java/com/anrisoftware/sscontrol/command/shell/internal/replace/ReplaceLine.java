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

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.command.shell.internal.replace.ParseSedSyntax.ParseSedSyntaxFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ReplaceLine {

    public interface ReplaceLineFactory {

        ReplaceLine create(Map<String, Object> args);

    }

    private final Map<String, Object> args;

    @Inject
    private ParseSedSyntaxFactory sedFactory;

    @Inject
    ReplaceLine(@Assisted Map<String, Object> args) {
        this.args = new HashMap<String, Object>(args);
    }

    public Map<String, Object> getArgs(Map<String, Object> a) {
        a = new HashMap<String, Object>(a);
        if (args.get("search") == null) {
            ParseSedSyntax sed;
            sed = sedFactory.create(args.get("replace").toString());
            sed.parse();
            a.put("search", sed.getSearch());
            a.put("replace", sed.getReplace());
        } else {
            a.put("search", args.get("search"));
            a.put("replace", args.get("replace"));
        }
        return a;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(args).toString();
    }

}
