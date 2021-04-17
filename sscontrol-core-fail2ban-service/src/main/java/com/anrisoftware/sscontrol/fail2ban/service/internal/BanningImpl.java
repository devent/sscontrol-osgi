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
package com.anrisoftware.sscontrol.fail2ban.service.internal;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Duration;

import com.anrisoftware.globalpom.core.durationformat.DurationFormatFactory;
import com.anrisoftware.sscontrol.fail2ban.service.external.Backend;
import com.anrisoftware.sscontrol.fail2ban.service.external.Banning;
import com.anrisoftware.sscontrol.fail2ban.service.external.Type;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Fail2ban</i> jail banning.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class BanningImpl implements Banning {

    private static final String ACTION_ARG = "action";

    private static final String APP_ARG = "app";

    private static final String TYPE_ARG = "type";

    private static final String BACKEND_ARG = "backend";

    private static final String TIME_ARG = "time";

    private static final String RETRIES_ARG = "retries";

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface BanningImplFactory {

        Banning create(Map<String, Object> args);

        Banning create();

    }

    private String app;

    private Integer retries;

    private Duration time;

    private Backend backend;

    private Type type;

    private String action;

    @Inject
    private DurationFormatFactory durationFormat;

    @AssistedInject
    BanningImpl() {
        this(new HashMap<String, Object>());
    }

    @AssistedInject
    BanningImpl(@Assisted Map<String, Object> args) {
        this.retries = (Integer) args.get(RETRIES_ARG);
        this.time = (Duration) args.get(TIME_ARG);
        this.backend = (Backend) args.get(BACKEND_ARG);
        this.type = (Type) args.get(TYPE_ARG);
        this.app = args.get(APP_ARG) == null ? null : args.get(APP_ARG).toString();
        this.action = args.get(ACTION_ARG) == null ? null : args.get(ACTION_ARG).toString();
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
        v = a.get(ACTION_ARG);
        if (v != null) {
            this.action = v.toString();
        }
    }

    @Override
    public Integer getRetries() {
        return retries;
    }

    @Override
    public Duration getTime() {
        return time;
    }

    @Override
    public Backend getBackend() {
        return backend;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getApp() {
        return app;
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(RETRIES_ARG, retries).append(TIME_ARG, time)
                .append(BACKEND_ARG, backend).append(TYPE_ARG, type).append(APP_ARG, app).append(ACTION_ARG, action)
                .toString();
    }

}
