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
package com.anrisoftware.sscontrol.command.shell.internal.facts;

import static org.apache.commons.lang3.StringUtils.split;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.types.host.external.SystemInfo;
import com.anrisoftware.sscontrol.utils.systemmappings.external.DefaultSystemInfoFactory;
import com.google.inject.assistedinject.Assisted;

/**
 *
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class CatReleaseParse implements Callable<SystemInfo> {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface CatReleaseParseFactory {

        CatReleaseParse create(@Assisted String output);

    }

    private final String output;

    @Inject
    private DefaultSystemInfoFactory systemFactory;

    @Inject
    CatReleaseParse(@Assisted String output) {
        this.output = output;
    }

    @Override
    public SystemInfo call() {
        Map<String, Object> args = new HashMap<>();
        String[] lines = split(output, '\n');
        for (String line : lines) {
            String[] value = split(line, '=');
            if (value.length != 2) {
                continue;
            }
            switch (value[0]) {
            case "ID":
                args.put("name", parseName(value[1]));
                break;
            case "VERSION_ID":
                args.put("version", value[1].replaceAll("\"", ""));
                break;
            default:
                break;
            }
        }
        return systemFactory.create(args);
    }

    private String parseName(String string) {
        String name = string.replaceAll("\"", "");
        return name;
    }
}
