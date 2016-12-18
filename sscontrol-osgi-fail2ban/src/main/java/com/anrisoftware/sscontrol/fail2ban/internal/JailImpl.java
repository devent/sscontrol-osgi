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

import static com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.stringListStatement;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Duration;

import com.anrisoftware.globalpom.arrays.ToList;
import com.anrisoftware.globalpom.durationformat.DurationFormatFactory;
import com.anrisoftware.sscontrol.fail2ban.external.Backend;
import com.anrisoftware.sscontrol.fail2ban.external.Jail;
import com.anrisoftware.sscontrol.fail2ban.external.Type;
import com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Fail2ban</i> jail.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class JailImpl implements Jail {

    private static final String APP_ARG = "app";

    private static final String TYPE_ARG = "type";

    private static final String BACKEND_ARG = "backend";

    private static final String TIME_ARG = "time";

    private static final String RETRIES_ARG = "retries";

    private static final String IGNORE_ARG = "ignore";

    private static final String NOTIFY_ARG = "notify";

    private static final String SERVICE_ARG = "service";

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

    private final List<String> ignoreAddresses;

    private String notify;

    private String app;

    private Integer retries;

    private Duration time;

    private Backend backend;

    private Type type;

    @Inject
    private DurationFormatFactory durationFormat;

    @AssistedInject
    JailImpl(@Assisted String service) {
        this.service = service;
        this.notify = null;
        this.ignoreAddresses = new ArrayList<>();
        this.retries = null;
        this.time = null;
        this.backend = null;
        this.type = null;
        this.app = null;
    }

    @SuppressWarnings("unchecked")
    @AssistedInject
    JailImpl(@Assisted Map<String, Object> args) {
        this.service = args.get(SERVICE_ARG).toString();
        this.notify = args.get(NOTIFY_ARG) == null ? null
                : args.get(NOTIFY_ARG).toString();
        this.ignoreAddresses = args.get(IGNORE_ARG) == null
                ? new ArrayList<String>()
                : new ArrayList<>((List<String>) args.get(IGNORE_ARG));
        this.retries = (Integer) args.get(RETRIES_ARG);
        this.time = (Duration) args.get(TIME_ARG);
        this.backend = (Backend) args.get(BACKEND_ARG);
        this.type = (Type) args.get(TYPE_ARG);
        this.app = args.get(APP_ARG) == null ? null
                : args.get(APP_ARG).toString();
    }

    public void notify(String address) throws ParseException {
        Map<String, Object> a = new HashMap<>();
        a.put("address", address);
        notify(a);
    }

    public void notify(Map<String, Object> args) throws ParseException {
        Map<String, Object> a = new HashMap<>(args);
        Object v;
        v = a.get("address");
        this.notify = v.toString();
    }

    public void banning(Map<String, Object> args) throws ParseException {
        Map<String, Object> a = new HashMap<>(args);
        Object v;
        v = a.get(RETRIES_ARG);
        if (v != null) {
            this.retries = (Integer) v;
        }
        v = a.get(TIME_ARG);
        if (v != null) {
            if (v instanceof Duration) {
                this.time = (Duration) v;
            } else {
                this.time = durationFormat.create().parse(v.toString());
            }
        }
        v = a.get(BACKEND_ARG);
        if (v != null) {
            this.backend = (Backend) v;
        }
        v = a.get(TYPE_ARG);
        if (v != null) {
            this.type = (Type) v;
        }
        v = a.get(APP_ARG);
        if (v != null) {
            this.app = v.toString();
        }
    }

    public void ignore(Map<String, Object> args) {
        Object v;
        v = args.get("address");
        if (v != null) {
            List<String> list = ToList.toList(v);
            this.ignoreAddresses.addAll(list);
        }
    }

    public List<String> getIgnore() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                ignoreAddresses.add(property);
            }
        });
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
        return ignoreAddresses;
    }

    @Override
    public Integer getBanningRetries() {
        return retries;
    }

    @Override
    public Duration getBanningTime() {
        return time;
    }

    @Override
    public Backend getBanningBackend() {
        return backend;
    }

    @Override
    public Type getBanningType() {
        return type;
    }

    @Override
    public String getBanningApp() {
        return app;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(service)
                .append(NOTIFY_ARG, notify).append(IGNORE_ARG, ignoreAddresses)
                .append(RETRIES_ARG, retries).append(TIME_ARG, time)
                .append(BACKEND_ARG, backend).append(TYPE_ARG, type)
                .append(APP_ARG, app).toString();
    }

}
