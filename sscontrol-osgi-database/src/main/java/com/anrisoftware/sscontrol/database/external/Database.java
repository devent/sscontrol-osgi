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
package com.anrisoftware.sscontrol.database.external;

import java.net.InetSocketAddress;
import java.util.List;

import com.anrisoftware.sscontrol.debug.external.DebugLogging;
import com.anrisoftware.sscontrol.types.external.Script;
import com.anrisoftware.sscontrol.types.external.UserPassword;

/**
 * Database script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Database extends Script {

    Database setBindAddress(InetSocketAddress address);

    InetSocketAddress getBindAddress();

    Database setAdminUser(UserPassword userPassword);

    UserPassword getAdminUser();

    List<DatabaseDb> getDatabases();

    List<DatabaseUser> getUsers();

    DebugLogging getDebug();
}
