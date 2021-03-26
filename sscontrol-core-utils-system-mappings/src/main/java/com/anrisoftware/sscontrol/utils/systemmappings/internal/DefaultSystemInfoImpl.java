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
package com.anrisoftware.sscontrol.utils.systemmappings.internal;

import static java.lang.String.format;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.generator.qdox.parser.ParseException;

import com.anrisoftware.sscontrol.types.host.external.SystemInfo;
import com.anrisoftware.sscontrol.utils.systemmappings.external.AbstractSystemInfo;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 *
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class DefaultSystemInfoImpl extends AbstractSystemInfo {

    @AssistedInject
    DefaultSystemInfoImpl(SystemNameMappingsProperties mappingsProperties,
            @Assisted String string) {
        super(parseSystemInfo(mappingsProperties, string));
    }

    private static SystemInfo parseSystemInfo(
            SystemNameMappingsProperties mappingsProperties, String string) {
        String[] split = StringUtils.split(string, "/");
        String s;
        String n;
        String v;
        if (split.length == 4) {
            s = split[1];
            n = split[2];
            v = split[3];
        } else if (split.length == 3) {
            n = split[1];
            v = split[2];
            s = mappingsProperties.getMapping(n);
        } else if (split.length == 2) {
            n = split[0];
            v = split[1];
            s = mappingsProperties.getMapping(n);
        } else {
            throw new ParseException(
                    format("Expected system/name/version, got '%s'", string), 0,
                    0);
        }
        final String version = v.toLowerCase(Locale.ENGLISH);
        final String system = s.toLowerCase(Locale.ENGLISH);
        final String name = n.toLowerCase(Locale.ENGLISH);
        return new SystemInfo() {

            @Override
            public String getVersion() {
                return version;
            }

            @Override
            public String getSystem() {
                return system;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }

    @AssistedInject
    DefaultSystemInfoImpl(SystemNameMappingsProperties mappingsProperties,
            @Assisted Map<String, Object> args) {
        super(parseArgs(mappingsProperties, args));
    }

    private static SystemInfo parseArgs(
            SystemNameMappingsProperties mappingsProperties,
            Map<String, Object> args) {
        final String name = parseName(args);
        final String system = parseSystem(mappingsProperties, name, args);
        final String version = parseVersion(args);
        return new SystemInfo() {

            @Override
            public String getVersion() {
                return version;
            }

            @Override
            public String getSystem() {
                return system;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }

    private static String parseVersion(Map<String, Object> args) {
        Object v = args.get("version");
        if (v != null) {
            return v.toString();
        } else {
            return null;
        }
    }

    private static String parseSystem(
            SystemNameMappingsProperties mappingsProperties, String name,
            Map<String, Object> args) {
        Object v = args.get("system");
        if (v != null) {
            return v.toString();
        } else {
            if (name != null) {
                return mappingsProperties.getMapping(name);
            } else {
                return null;
            }
        }
    }

    private static String parseName(Map<String, Object> args) {
        Object v = args.get("name");
        if (v != null) {
            return v.toString();
        } else {
            return null;
        }
    }

}
