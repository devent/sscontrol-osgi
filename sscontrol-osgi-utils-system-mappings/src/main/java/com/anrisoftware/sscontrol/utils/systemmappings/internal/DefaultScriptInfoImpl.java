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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.anrisoftware.sscontrol.types.host.external.SystemInfo;
import com.anrisoftware.sscontrol.utils.systemmappings.external.AbstractScriptInfo;
import com.anrisoftware.sscontrol.utils.systemmappings.external.DefaultSystemInfoFactory;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class DefaultScriptInfoImpl extends AbstractScriptInfo {

    private final String service;

    private final SystemInfo system;

    @AssistedInject
    DefaultScriptInfoImpl(DefaultSystemInfoFactory systemFactory,
            @Assisted String name) {
        this.system = systemFactory.parse(name);
        String[] split = StringUtils.split(name, "/");
        this.service = split[0];
    }

    @AssistedInject
    DefaultScriptInfoImpl(DefaultSystemInfoFactory systemFactory,
            @Assisted Map<String, Object> args) {
        this.system = systemFactory.create(args);
        Object v = args.get("service");
        assertThat("service=null", v, notNullValue());
        this.service = v.toString();
    }

    public SystemInfo getSystemInfo() {
        return system;
    }

    @Override
    public String getService() {
        return service;
    }

    @Override
    public String getSystem() {
        return system.getSystem();
    }

    @Override
    public String getName() {
        return system.getName();
    }

    @Override
    public String getVersion() {
        return system.getVersion();
    }

}
