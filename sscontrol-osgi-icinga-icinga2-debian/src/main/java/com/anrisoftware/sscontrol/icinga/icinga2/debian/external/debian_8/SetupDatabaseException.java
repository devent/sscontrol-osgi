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
package com.anrisoftware.sscontrol.icinga.icinga2.debian.external.debian_8;

import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.sscontrol.icinga.service.external.Plugin;
import com.anrisoftware.sscontrol.types.app.external.AppException;

/**
 * Thrown if there was an error setup the database.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@SuppressWarnings("serial")
public class SetupDatabaseException extends AppException {

    public SetupDatabaseException(Plugin plugin, ProcessTask task) {
        super("Error setup database");
        addContextValue("plugin", plugin);
        addContextValue("task", task);
        addContextValue("error", task.getErr());
        addContextValue("exit-value", task.getExitValue());
    }
}
