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

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.core.resources.ToURI;
import com.anrisoftware.sscontrol.k8s.backup.service.external.Client;
import com.google.inject.assistedinject.Assisted;

/**
 * Backup client.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ClientImpl implements Client {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ClientImplFactory {

        Client create(Map<String, Object> args);

    }

    private URI key;

    private String config;

    @Inject
    ClientImpl(@Assisted Map<String, Object> args) {
        parseArgs(args);
    }

    public void setKey(URI key) {
        this.key = key;
    }

    @Override
    public URI getKey() {
        return key;
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
        parseKey(args);
    }

    private void parseConfig(Map<String, Object> args) {
        Object v = args.get("config");
        if (v != null) {
            setConfig(v.toString());
        }
    }

    private void parseKey(Map<String, Object> args) {
        Object v = args.get("key");
        if (v != null) {
            setKey(ToURI.toURI(v).convert());
        }
    }

}
