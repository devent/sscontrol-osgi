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
package com.anrisoftware.sscontrol.k8s.backup.client.external;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Service for backup.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class AbstractService implements Service {

    private String name;

    private String namespace;

    private String source;

    private String dest;

    protected AbstractService(Map<String, Object> args) {
        parseArgs(args);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String getSource() {
        return source;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    @Override
    public String getDest() {
        return dest;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        parseName(args);
        parseNamespace(args);
        parseSource(args);
        parseDest(args);
    }

    private void parseSource(Map<String, Object> args) {
        Object v = args.get("source");
        if (v != null) {
            setSource(v.toString());
        }
    }

    private void parseDest(Map<String, Object> args) {
        Object v = args.get("dest");
        if (v != null) {
            setDest(v.toString());
        }
    }

    private void parseName(Map<String, Object> args) {
        Object v = args.get("name");
        assertThat("name=null", v, notNullValue());
        setName(v.toString());
    }

    private void parseNamespace(Map<String, Object> args) {
        Object v = args.get("namespace");
        assertThat("namespace=null", v, notNullValue());
        setNamespace(v.toString());
    }

}
