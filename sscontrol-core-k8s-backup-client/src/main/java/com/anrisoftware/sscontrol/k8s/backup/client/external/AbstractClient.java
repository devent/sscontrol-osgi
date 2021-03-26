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
package com.anrisoftware.sscontrol.k8s.backup.client.external;

import java.net.URI;
import java.text.ParseException;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Duration;

import com.anrisoftware.globalpom.core.durationformat.DurationFormatFactory;
import com.anrisoftware.globalpom.core.durationsimpleformat.DurationSimpleFormatFactory;
import com.anrisoftware.globalpom.core.durationsimpleformat.UnitMultiplier;
import com.anrisoftware.globalpom.core.resources.ToURI;

/**
 * Backup client.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class AbstractClient implements Client {

    private URI key;

    private String config;

    private Boolean proxy;

    private Duration timeout;

    private transient DurationSimpleFormatFactory durationSimpleFormatFactory;

    private transient DurationFormatFactory durationFormatFactory;

    protected AbstractClient(Map<String, Object> args,
            DurationSimpleFormatFactory durationSimpleFormatFactory,
            DurationFormatFactory durationFormatFactory) throws ParseException {
        this.durationSimpleFormatFactory = durationSimpleFormatFactory;
        this.durationFormatFactory = durationFormatFactory;
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

    public void setProxy(Boolean proxy) {
        this.proxy = proxy;
    }

    @Override
    public Boolean getProxy() {
        return proxy;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    @Override
    public Duration getTimeout() {
        return timeout;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) throws ParseException {
        parseConfig(args);
        parseKey(args);
        parseProxy(args);
        parseTimeout(args);
    }

    private void parseTimeout(Map<String, Object> args) throws ParseException {
        Object v = args.get("timeout");
        if (v != null) {
            if (v instanceof Duration) {
                setTimeout((Duration) v);
            } else {
                try {
                    parseSimpleTimeout(v.toString());
                } catch (ParseException e) {
                    parseDurationTimeout(v.toString());
                }
            }
        }
    }

    private void parseDurationTimeout(String s) throws ParseException {
        Duration duration = durationFormatFactory.create().parse(s);
        setTimeout(duration);
    }

    private void parseSimpleTimeout(String s) throws ParseException {
        long seconds = durationSimpleFormatFactory.create().parse(s,
                UnitMultiplier.SECONDS);
        setTimeout(Duration.standardSeconds(seconds));
    }

    private void parseProxy(Map<String, Object> args) {
        Object v = args.get("proxy");
        if (v != null) {
            if (v instanceof Boolean) {
                Boolean b = (Boolean) v;
                setProxy(b);
            }
        }
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
