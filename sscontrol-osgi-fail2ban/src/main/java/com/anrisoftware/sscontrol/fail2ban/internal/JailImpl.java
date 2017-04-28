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
package com.anrisoftware.sscontrol.fail2ban.internal;

import static com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.stringListStatement;
import static org.codehaus.groovy.runtime.InvokerHelper.invokeMethod;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.core.arrays.ToList;
import com.anrisoftware.sscontrol.fail2ban.external.Banning;
import com.anrisoftware.sscontrol.fail2ban.external.Jail;
import com.anrisoftware.sscontrol.fail2ban.internal.BanningImpl.BanningImplFactory;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Fail2ban</i> jail.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class JailImpl implements Jail {

    private static final String ENABLED_ARG = "enabled";

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

    private final Banning banning;

    private Boolean enabled;

    private String notify;

    @AssistedInject
    JailImpl(BanningImplFactory banningFactory, @Assisted String service) {
        this.service = service;
        this.notify = null;
        this.ignoreAddresses = new ArrayList<>();
        this.enabled = null;
        this.banning = banningFactory.create();
    }

    @SuppressWarnings("unchecked")
    @AssistedInject
    JailImpl(BanningImplFactory banningFactory,
            @Assisted Map<String, Object> args) {
        this.service = args.get(SERVICE_ARG).toString();
        this.notify = args.get(NOTIFY_ARG) == null ? null
                : args.get(NOTIFY_ARG).toString();
        this.ignoreAddresses = args.get(IGNORE_ARG) == null
                ? new ArrayList<String>()
                : new ArrayList<>((List<String>) args.get(IGNORE_ARG));
        this.enabled = args.get(ENABLED_ARG) == null ? null
                : (Boolean) args.get(ENABLED_ARG);
        this.banning = banningFactory.create(args);
    }

    public void enabled(Boolean enabled) throws ParseException {
        Map<String, Object> a = new HashMap<>();
        a.put("enabled", enabled);
        enabled(a);
    }

    public void enabled(Map<String, Object> args) throws ParseException {
        Map<String, Object> a = new HashMap<>(args);
        Object v;
        v = a.get("enabled");
        this.enabled = (Boolean) v;
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
        invokeMethod(banning, "banning", a);
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
    public Boolean getEnabled() {
        return enabled;
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
    public Banning getBanning() {
        return banning;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(service)
                .append(ENABLED_ARG, enabled).append(NOTIFY_ARG, notify)
                .append(IGNORE_ARG, ignoreAddresses).append("banning", banning)
                .toString();
    }

}
