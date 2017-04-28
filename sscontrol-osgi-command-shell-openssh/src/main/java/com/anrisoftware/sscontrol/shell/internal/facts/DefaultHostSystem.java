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

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.types.host.external.SystemInfo.AbstractSystemInfo;
import com.anrisoftware.sscontrol.utils.systemmappings.external.SystemNameMappingsProperties;
import com.google.inject.assistedinject.Assisted;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class DefaultHostSystem extends AbstractSystemInfo {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface DefaultHostSystemFactory {

        DefaultHostSystem create(@Assisted Map<String, Object> args);

    }

    private String name;

    private String version;

    private String system;

    @Inject
    DefaultHostSystem(SystemNameMappingsProperties mappingsProperties,
            @Assisted Map<String, Object> args) {
        this.name = args.get("name").toString();
        Object v = args.get("system");
        if (v != null) {
            this.system = v.toString();
        } else {
            this.system = mappingsProperties.getMapping(name);
        }
        this.version = args.get("version").toString();
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
