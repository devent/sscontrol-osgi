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
package com.anrisoftware.sscontrol.shell.internal.facts;

import static org.apache.commons.lang3.StringUtils.split;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.shell.internal.facts.DefaultHostSystem.DefaultHostSystemFactory;
import com.anrisoftware.sscontrol.types.external.host.HostSystem;
import com.google.inject.assistedinject.Assisted;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class CatReleaseParse implements Callable<HostSystem> {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface CatReleaseParseFactory {

        CatReleaseParse create(@Assisted String output);

    }

    private final String output;

    @Inject
    private DefaultHostSystemFactory hostSystemFactory;

    @Inject
    CatReleaseParse(@Assisted String output) {
        this.output = output;
    }

    @Override
    public HostSystem call() {
        Map<String, Object> args = new HashMap<>();
        String[] lines = split(output, '\n');
        for (String line : lines) {
            String[] value = split(line, '=');
            if (value.length != 2) {
                continue;
            }
            switch (value[0]) {
            case "ID":
                args.put("name", value[1]);
                break;
            case "VERSION_ID":
                args.put("version", value[1].replaceAll("\"", ""));
                break;
            default:
                break;
            }
        }
        return hostSystemFactory.create(args);
    }
}
