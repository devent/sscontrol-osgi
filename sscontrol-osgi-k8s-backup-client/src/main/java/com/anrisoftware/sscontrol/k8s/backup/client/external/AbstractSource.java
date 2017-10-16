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

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Backup source.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class AbstractSource implements Source {

    private String source;

    private String chown;

    private String chmod;

    protected AbstractSource(Map<String, Object> args) {
        parseArgs(args);
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String getSource() {
        return source;
    }

    public void setTarget(String target) {
        setSource(target);
    }

    @Override
    public String getTarget() {
        return getSource();
    }

    public void setChown(String chown) {
        this.chown = chown;
    }

    @Override
    public String getChown() {
        return chown;
    }

    public void setChmod(String chmod) {
        this.chmod = chmod;
    }

    @Override
    public String getChmod() {
        return chmod;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        parseSource(args);
        parseTarget(args);
        parseChown(args);
        parseChmod(args);
    }

    private void parseChmod(Map<String, Object> args) {
        Object v = args.get("chmod");
        if (v != null) {
            setChmod(v.toString());
        }
    }

    private void parseChown(Map<String, Object> args) {
        Object v = args.get("chown");
        if (v != null) {
            setChown(v.toString());
        }
    }

    private void parseSource(Map<String, Object> args) {
        Object v = args.get("source");
        if (v != null) {
            setSource(v.toString());
        }
    }

    private void parseTarget(Map<String, Object> args) {
        Object v = args.get("target");
        if (v != null) {
            setTarget(v.toString());
        }
    }

}
