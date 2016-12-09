/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-fail2ban.
 *
 * sscontrol-security-fail2ban is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-fail2ban is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-fail2ban. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.fail2ban;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Duration;

import com.anrisoftware.globalpom.format.duration.DurationFormatFactory;
import com.anrisoftware.sscontrol.core.groovy.statementsmap.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.statementsmap.StatementsMapFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Fail2ban</i> jail.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Jail {

    private static final String APP_KEY = "app";

    private static final String TYPE_KEY = "type";

    private static final String BACKEND_KEY = "backend";

    private static final String TIME_KEY = "time";

    private static final String RETRIES_KEY = "retries";

    private static final String ADDRESS_KEY = "address";

    private static final String BANNING_KEY = "banning";

    private static final String IGNORE_KEY = "ignore";

    private static final String NOTIFY_KEY = "notify";

    private static final String SERVICE_KEY = "service";

    private static final String NAME = "jail";

    private static final String ADDRESSES_KEY = "addresses";

    private final StatementsMap statementsMap;

    private final String service;

    @Inject
    private DurationFormatFactory durationFormatFactory;

    /**
     * @see JailFactory#create(Map)
     */
    @Inject
    Jail(StatementsMapFactory statementsMapFactory,
            @Assisted Map<String, Object> args) {
        this.statementsMap = createStatementMap(args, statementsMapFactory);
        this.service = args.get(SERVICE_KEY).toString();
    }

    private StatementsMap createStatementMap(Map<String, Object> args,
            StatementsMapFactory factory) {
        StatementsMap map = factory.create(this, NAME);
        map.addAllowed(SERVICE_KEY, IGNORE_KEY, BANNING_KEY);
        map.setAllowValue(true, SERVICE_KEY);
        map.addAllowedKeys(SERVICE_KEY, NOTIFY_KEY);
        map.addAllowedKeys(IGNORE_KEY, ADDRESS_KEY, ADDRESSES_KEY);
        map.addAllowedKeys(BANNING_KEY, RETRIES_KEY, TIME_KEY, BACKEND_KEY,
                TYPE_KEY, APP_KEY);
        map.putValue(SERVICE_KEY, args.get(SERVICE_KEY));
        if (args.containsKey(NOTIFY_KEY)) {
            map.putMapValue(SERVICE_KEY, NOTIFY_KEY, args.get(NOTIFY_KEY));
        }
        return map;
    }

    /**
     * Returns the jail service name.
     * <p>
     *
     * <pre>
     * jail "apache", {
     * }
     * </pre>
     *
     * @return the jail service {@link String} name.
     */
    public String getService() {
        return statementsMap.value(SERVICE_KEY);
    }

    /**
     * Returns the email address for notification.
     * <p>
     *
     * <pre>
     * jail "apache", notify: "root@localhost", {
     * }
     * </pre>
     *
     * @return the email {@link String} address.
     */
    public String getNotify() {
        return statementsMap.mapValue(SERVICE_KEY, NOTIFY_KEY);
    }

    /**
     * Returns the addresses to ignore.
     * <p>
     *
     * <pre>
     * jail "apache", {
     *     ignore address: "192.0.0.1" // or
     *     ignore addresses: ["192.0.0.1", "192.0.0.2"]
     * }
     * </pre>
     *
     * @return the {@link List} addresses or {@code null}.
     */
    public List<String> getIgnoreAddresses() {
        List<String> list = new ArrayList<String>();
        StatementsMap map = statementsMap;
        String address = map.mapValue(IGNORE_KEY, ADDRESS_KEY);
        if (address != null) {
            list.add(address);
        }
        List<String> addresses = map.mapValueAsStringList(IGNORE_KEY,
                ADDRESSES_KEY);
        if (addresses != null) {
            list.addAll(addresses);
        }
        return list.size() == 0 ? null : list;
    }

    /**
     * Returns the maximum retries.
     * <p>
     *
     * <pre>
     * jail "apache", {
     *     banning retries: 3
     * }
     * </pre>
     *
     * @return the maximum {@link Integer} retries or {@code null}.
     */
    public Integer getBanningRetries() {
        return statementsMap.mapValue(BANNING_KEY, RETRIES_KEY);
    }

    /**
     * Returns the banning time.
     * <p>
     *
     * <pre>
     * jail "apache", {
     *     banning time: "PT10M"
     * }
     * </pre>
     *
     * @return the banning time {@link Duration} duration or {@code null}.
     *
     * @throws ParseException
     *             if the string could not be parsed.
     */
    public Duration getBanningTime() throws ParseException {
        Object value = statementsMap.mapValue(BANNING_KEY, TIME_KEY);
        if (value == null) {
            return null;
        }
        if (value instanceof Duration) {
            return (Duration) value;
        } else {
            return durationFormatFactory.create().parse(value.toString());
        }
    }

    /**
     * Returns the banning backend.
     * <p>
     *
     * <pre>
     * jail "apache", {
     *     banning backend: Backend.polling
     * }
     * </pre>
     *
     * @return the banning {@link Backend} backend or {@code null}.
     */
    public Backend getBanningBackend() throws ParseException {
        Object value = statementsMap.mapValue(BANNING_KEY, BACKEND_KEY);
        if (value == null) {
            return null;
        }
        if (value instanceof Backend) {
            return (Backend) value;
        } else {
            return Backend.valueOf(value.toString());
        }
    }

    /**
     * Returns the banning type.
     * <p>
     *
     * <pre>
     * jail "apache", {
     *     banning type: Type.deny
     * }
     * </pre>
     *
     * @return the banning {@link Type} type or {@code null}.
     */
    public Type getBanningType() throws ParseException {
        Object value = statementsMap.mapValue(BANNING_KEY, TYPE_KEY);
        if (value == null) {
            return null;
        }
        if (value instanceof Type) {
            return (Type) value;
        } else {
            return Type.valueOf(value.toString());
        }
    }

    /**
     * Returns the banning app.
     * <p>
     *
     * <pre>
     * jail "apache", {
     *     banning app: "OpenSSH"
     * }
     * </pre>
     *
     * @return the banning {@link String} app or {@code null}.
     */
    public String getBanningApp() throws ParseException {
        return statementsMap.mapValue(BANNING_KEY, APP_KEY);
    }

    public Object methodMissing(String name, Object args) {
        return statementsMap.methodMissing(name, args);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(SERVICE_KEY, service)
                .toString();
    }
}
