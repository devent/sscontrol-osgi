/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
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
import static org.apache.commons.lang3.Validate.notNull;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.resources.StringToURI;
import com.anrisoftware.sscontrol.ssh.internal.SshHostSystemImpl.SshHostSystemImplFactory;
import com.anrisoftware.sscontrol.types.external.HostSystem;
import com.anrisoftware.sscontrol.types.external.SshHost;

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

        SshHostImpl create();

    }

    private transient SshHostSystemImplFactory systemFactory;

    private String host;

    private String user;

    private Integer port;

    private URI key;

    private HostSystem system;

    @Inject
    SshHostImpl(SshHostSystemImplFactory systemFactory) {
        this.systemFactory = systemFactory;
        this.system = systemFactory.create(new HashMap<String, Object>());
    }

    public void host(String host) {
        Map<String, Object> args = new HashMap<>();
        host(args, host);
    }

    public void host(Map<String, Object> args, String host) {
        String[] userHostPort = split(host, "@");
        switch (userHostPort.length) {
        case 1:
            args.put("host", userHostPort[0]);
            break;
        case 2:
            args.put("user", userHostPort[0]);
            String[] hostPort = split(userHostPort[1], ":");
            args.put("host", hostPort[0]);
            if (hostPort.length > 1) {
                args.put("port", Integer.valueOf(hostPort[1]));
            }
            break;
        default:
            break;
        }
        host(args);
    }

    public void host(Map<String, Object> args) {
        Object v;
        v = args.get("host");
        notNull(v, "host=null");
        this.host = v.toString();
        v = args.get("user");
        if (v != null) {
            this.user = v.toString();
        }
        v = args.get("port");
        if (v != null) {
            this.port = (Integer) args.get("port");
        }
        v = args.get("key");
        if (v != null) {
            this.key = StringToURI.toURI(v.toString());
        }
        v = args.get("system");
        if (v != null) {
            this.system = systemFactory.create(args);
        }
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
    public HostSystem getSystem() {
        return system;
    }

    @Override
    public String getHostAddress() throws UnknownHostException {
        return InetAddress.getByName(host).getHostAddress();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("user", user)
                .append("host", host).append("port", port).append("key", key)
                .append("system", system).toString();
    }

}
