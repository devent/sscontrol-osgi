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
package com.anrisoftware.sscontrol.k8s.backup.service.internal;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8s.backup.service.external.Ssh;
import com.google.inject.assistedinject.Assisted;

/**
 * Ssh client.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class SshImpl implements Ssh {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface SshImplFactory {

        Ssh create(Map<String, Object> args);

    }

    private String config;

    @Inject
    SshImpl(@Assisted Map<String, Object> args) {
        parseArgs(args);
    }

    public void setConfig(String config) {
        this.config = config;
    }

    @Override
    public String getConfig() {
        return config;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        parseConfig(args);
    }

    private void parseConfig(Map<String, Object> args) {
        Object v = args.get("config");
        if (v != null) {
            setConfig(v.toString());
        }
    }

}
