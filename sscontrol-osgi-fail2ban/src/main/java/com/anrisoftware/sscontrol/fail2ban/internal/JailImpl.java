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
package com.anrisoftware.sscontrol.fail2ban.internal;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Duration;

import com.anrisoftware.sscontrol.fail2ban.external.Backend;
import com.anrisoftware.sscontrol.fail2ban.external.Jail;
import com.anrisoftware.sscontrol.fail2ban.external.Type;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Fail2ban</i> jail.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class JailImpl implements Jail {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface JailImplFactory {

        Jail create(Map<String, Object> args);

        Jail create(String string);

    }

    private final String service;

    private final String notify;

    private final List<String> ignore;

    private final Integer retries;

    private final Duration time;

    private final Backend backend;

    private final Type type;

    private final String app;

    @AssistedInject
    JailImpl(@Assisted String service) {
        this.service = service;
        this.notify = null;
        this.ignore = null;
        this.retries = null;
        this.time = null;
        this.backend = null;
        this.type = null;
        this.app = null;
    }

    @SuppressWarnings("unchecked")
    @AssistedInject
    JailImpl(@Assisted Map<String, Object> args) {
        this.service = args.get("service").toString();
        this.notify = args.get("notify").toString();
        this.ignore = new ArrayList<>((List<String>) args.get("ignore"));
        this.retries = ((Number) args.get("retries")).intValue();
        this.time = (Duration) args.get("time");
        this.backend = (Backend) args.get("backend");
        this.type = (Type) args.get("type");
        this.app = args.get("notify").toString();
    }

    @Override
    public String getService() {
        return service;
    }

    @Override
    public String getNotify() {
        return notify;
    }

    @Override
    public List<String> getIgnoreAddresses() {
        return ignore;
    }

    @Override
    public Integer getBanningRetries() {
        return retries;
    }

    @Override
    public Duration getBanningTime() throws ParseException {
        return time;
    }

    @Override
    public Backend getBanningBackend() throws ParseException {
        return backend;
    }

    @Override
    public Type getBanningType() throws ParseException {
        return type;
    }

    @Override
    public String getBanningApp() throws ParseException {
        return app;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(service)
                .append("notify", notify).append("ignore", ignore)
                .append("retries", retries).append("time", time)
                .append("backend", backend).append("type", type)
                .append("app", app).toString();
    }

}
