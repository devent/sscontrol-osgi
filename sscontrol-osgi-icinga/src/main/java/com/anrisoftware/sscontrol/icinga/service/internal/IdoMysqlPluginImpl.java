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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.icinga.service.external.Database;
import com.anrisoftware.sscontrol.icinga.service.external.IdoMysqlPlugin;
import com.anrisoftware.sscontrol.icinga.service.internal.DatabaseImpl.DatabaseImplFactory;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * ido-mysql plugin.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class IdoMysqlPluginImpl implements IdoMysqlPlugin {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface IdoMysqlPluginImplFactory extends PluginFactory {

    }

    private final String name;

    private Database database;

    private transient DatabaseImplFactory databaseFactory;

    private transient IdoMysqlPluginImplLogger log;

    @AssistedInject
    IdoMysqlPluginImpl(IdoMysqlPluginImplLogger log,
            DatabaseImplFactory databaseFactory) {
        this(new HashMap<String, Object>(), log, databaseFactory);
    }

    @AssistedInject
    IdoMysqlPluginImpl(@Assisted Map<String, Object> args,
            IdoMysqlPluginImplLogger log, DatabaseImplFactory databaseFactory) {
        this.log = log;
        this.databaseFactory = databaseFactory;
        Object v = args.get("name");
        this.name = v.toString();
    }

    public void database(Map<String, Object> args) {
        Database db = databaseFactory.create(args);
        this.database = db;
        log.databaseSet(this, db);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Database getDatabase() {
        return database;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
