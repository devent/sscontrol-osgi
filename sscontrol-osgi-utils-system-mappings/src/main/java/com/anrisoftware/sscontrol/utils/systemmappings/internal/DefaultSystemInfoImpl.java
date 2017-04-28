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
package com.anrisoftware.sscontrol.utils.systemmappings.internal;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.anrisoftware.sscontrol.utils.systemmappings.external.AbstractSystemInfo;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class DefaultSystemInfoImpl extends AbstractSystemInfo {

    private String name;

    private String version;

    private String system;

    @AssistedInject
    DefaultSystemInfoImpl(SystemNameMappingsProperties mappingsProperties,
            @Assisted String string) {
        String[] split = StringUtils.split(string, "/");
        if (split.length == 4) {
            this.system = split[1];
            this.name = split[2];
            this.version = split[3];
        }
        if (split.length == 3) {
            this.name = split[1];
            this.version = split[2];
            this.system = mappingsProperties.getMapping(name);
        }
        if (split.length == 2) {
            this.name = split[0];
            this.version = split[1];
            this.system = mappingsProperties.getMapping(name);
        }
    }

    @AssistedInject
    DefaultSystemInfoImpl(SystemNameMappingsProperties mappingsProperties,
            @Assisted Map<String, Object> args) {
        parseName(args);
        parseSystem(mappingsProperties, args);
        parseVersion(args);
    }

    private void parseVersion(Map<String, Object> args) {
        Object v = args.get("version");
        if (v != null) {
            this.version = v.toString();
        }
    }

    private void parseSystem(SystemNameMappingsProperties mappingsProperties,
            Map<String, Object> args) {
        Object v = args.get("system");
        if (v != null) {
            this.system = v.toString();
        } else {
            if (name != null) {
                this.system = mappingsProperties.getMapping(name);
            }
        }
    }

    private void parseName(Map<String, Object> args) {
        Object v = args.get("name");
        if (v != null) {
            this.name = v.toString();
        }
    }

    public void setSystem(String system) {
        this.system = system;
    }

    @Override
    public String getSystem() {
        return system;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String getVersion() {
        return version;
    }

}
