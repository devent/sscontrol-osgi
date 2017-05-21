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

import static org.apache.commons.lang3.StringUtils.split;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.core.resources.ToURI;
import com.anrisoftware.sscontrol.types.host.external.SystemInfo;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.anrisoftware.sscontrol.utils.systemmappings.external.DefaultSystemInfoFactory;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class SshHostImpl implements SshHost {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface SshHostImplFactory {

        SshHost create();

        SshHost create(Map<String, Object> args);
    }

    private transient DefaultSystemInfoFactory systemFactory;

    private final String host;

    private final String user;

    private final Integer port;

    private final URI key;

    private final SystemInfo system;

    private final File socket;

    @AssistedInject
    SshHostImpl(DefaultSystemInfoFactory systemFactory) {
        this(systemFactory, new HashMap<String, Object>());
    }

    @AssistedInject
    SshHostImpl(DefaultSystemInfoFactory systemFactory,
            @Assisted Map<String, Object> args) {
        this.systemFactory = systemFactory;
        Map<String, Object> a = splitHost(args);
        this.host = parseHost(a);
        this.user = parseUser(a);
        this.port = parsePort(a);
        this.key = parseKey(args);
        this.system = parseSystem(args);
        this.socket = parseSocket(args);
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    @Override
    public URI getKey() {
        return key;
    }

    @Override
    public SystemInfo getSystem() {
        return system;
    }

    @Override
    public File getSocket() {
        return socket;
    }

    @Override
    public String getHostAddress() throws UnknownHostException {
        return InetAddress.getByName(host).getHostAddress();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private Map<String, Object> splitHost(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<>(args);
        String host = parseHost(args);
        String[] userHostPort = split(host, "@");
        switch (userHostPort.length) {
        case 1:
            a.put("host", userHostPort[0]);
            break;
        case 2:
            a.put("user", userHostPort[0]);
            String[] hostPort = split(userHostPort[1], ":");
            a.put("host", hostPort[0]);
            if (hostPort.length > 1) {
                a.put("port", Integer.valueOf(hostPort[1]));
            }
            break;
        default:
            break;
        }
        return a;
    }

    private String parseHost(Map<String, Object> args) {
        Object v = args.get("host");
        assertThat("host=null", v, notNullValue());
        assertThat("host=empty", v.toString(), not(isEmptyString()));
        return v.toString();
    }

    private String parseUser(Map<String, Object> args) {
        Object v = args.get("user");
        return v != null ? v.toString() : null;
    }

    private Integer parsePort(Map<String, Object> args) {
        Object v = args.get("port");
        return (Integer) v;
    }

    private URI parseKey(Map<String, Object> args) {
        Object v = args.get("key");
        return v != null ? ToURI.toURI(v) : null;
    }

    private SystemInfo parseSystem(Map<String, Object> args) {
        Object v = args.get("system");
        if (v == null) {
            return systemFactory.create(new HashMap<String, Object>());
        }
        if (v instanceof SystemInfo) {
            return (SystemInfo) v;
        } else {
            return systemFactory.parse(v.toString());
        }
    }

    private File parseSocket(Map<String, Object> args) {
        Object v = args.get("socket");
        if (v == null) {
            return null;
        }
        if (v instanceof File) {
            return (File) v;
        } else {
            return new File(v.toString());
        }
    }

}
