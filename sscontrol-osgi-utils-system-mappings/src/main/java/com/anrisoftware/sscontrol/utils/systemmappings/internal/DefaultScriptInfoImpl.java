/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
