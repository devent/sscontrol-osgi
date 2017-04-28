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
package com.anrisoftware.sscontrol.ssh.internal;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.anrisoftware.sscontrol.types.host.external.SystemInfo;
import com.anrisoftware.sscontrol.types.host.external.SystemInfo.AbstractSystemInfo;
import com.anrisoftware.sscontrol.utils.systemmappings.external.SystemNameMappingsProperties;
import com.google.inject.assistedinject.Assisted;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class SshHostSystemImpl extends AbstractSystemInfo {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface SshHostSystemImplFactory {

        SystemInfo create(@Assisted Map<String, Object> args);

    }

    private String version;

    private String name;

    private String system;

    @Inject
    public SshHostSystemImpl(SystemNameMappingsProperties mappingsProperties,
            @Assisted Map<String, Object> args) {
        parseArgs(mappingsProperties, args);
    }

    @Override
    public String getSystem() {
        return system;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    private void parseArgs(SystemNameMappingsProperties mappingsProperties,
            Map<String, Object> args) {
        Object v = args.get("system");
        if (v != null) {
            parseSystem(mappingsProperties, v.toString());
        }
    }

    private void parseSystem(SystemNameMappingsProperties mappingsProperties,
            String string) {
        String[] split = StringUtils.split(string, "/");
        int i = 0;
        String system = null;
        String name;
        String version;
        if (split.length == 3) {
            system = split[i++];
        }
        name = split[i++];
        if (system == null) {
            system = mappingsProperties.getMapping(name);
        }
        version = split[i++];
        this.system = system;
        this.name = name;
        this.version = version;
    }

}
