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
package com.anrisoftware.sscontrol.icinga.service.internal;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.icinga.service.external.Database;
import com.google.inject.assistedinject.Assisted;

/**
 * Database credentials.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class DatabaseImpl implements Database {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface DatabaseImplFactory {

        Database create(Map<String, Object> args);

    }

    private String host;

    private String user;

    private String password;

    private String database;

    @Inject
    DatabaseImpl(@Assisted Map<String, Object> args) {
        Object v = args.get("host");
        if (v != null) {
            this.host = v.toString();
        }
        v = args.get("user");
        if (v != null) {
            this.user = v.toString();
        }
        v = args.get("password");
        if (v != null) {
            this.password = v.toString();
        }
        v = args.get("database");
        if (v != null) {
            this.database = v.toString();
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
    public String getPassword() {
        return password;
    }

    @Override
    public String getDatabase() {
        return database;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
