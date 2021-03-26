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
package com.anrisoftware.sscontrol.k8scluster.service.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.types.cluster.external.Credentials;

/**
 * Parses the name host and port for the credentials.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public abstract class AbstractCredentials implements Credentials {

    private String name;

    private final AbstractCredentialsLogger log;

    private String host;

    private Integer port;

    protected AbstractCredentials(AbstractCredentialsLogger log,
            Map<String, Object> args) {
        this.log = log;
        parseArgs(args);
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getHost() {
        return host;
    }

    public void setPort(int port) {
        assertThat("port>0", port, greaterThan(0));
        this.port = port;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    public void setName(String name) {
        this.name = name;
        log.nameSet(this, name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        parseType(args);
        parseName(args);
        parsePort(args);
    }

    private void parsePort(Map<String, Object> args) {
        Object v;
        v = args.get("port");
        if (v != null) {
            setPort((Integer) v);
        }
    }

    private void parseName(Map<String, Object> args) {
        Object v = args.get("name");
        if (v != null) {
            setName(v.toString());
        }
    }

    private void parseType(Map<String, Object> args) {
        Object v = args.get("type");
        assertThat("type=null", v, notNullValue());
        assertThat(String.format("type=%s", getType()), v.toString(),
                equalToIgnoringCase(getType()));
    }

}
