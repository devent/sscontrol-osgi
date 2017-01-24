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
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.types.external.HostSystem;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class SshHostSystemImpl implements HostSystem {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface SshHostSystemImplFactory {

        HostSystem create(@Assisted Map<String, Object> args);

    }

    private String version;

    private String name;

    @Inject
    public SshHostSystemImpl(@Assisted Map<String, Object> args) {
        parseArgs(args);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name)
                .append("version", version).toString();
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("system");
        if (v != null) {
            parseSystem(v.toString());
        }
    }

    private void parseSystem(String string) {
        String[] split = StringUtils.split(string, "-");
        this.name = split[0];
        this.version = split[1];
    }

}
