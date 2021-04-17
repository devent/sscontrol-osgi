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
package com.anrisoftware.sscontrol.template.external;

import java.util.Map;

import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * Template command.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public interface Template {

    static final String BASE_ARG = "base";

    static final String ATTRIBUTES_ARG = "attributes";

    static final String CLASS_LOADER_ARG = "classLoader";

    static final String CONTROL_ARG = "control";

    static final String RESOURCE_ARG = "resource";

    static final String LOCALE_ARG = "locale";

    static final String VARS_ARG = "vars";

    static final String NAME_ARG = "name";

    static final String CHARSET_ARG = "charset";

    static final String SRC_ARG = "src";

    static final String DEST_ARG = "dest";

    /**
     * Factory to create the template command.
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface TemplateFactory {

        Template create(@Assisted Map<String, Object> args,
                @Assisted SshHost target, @Assisted("parent") Object parent,
                @Assisted Threads threads, @Assisted("log") Object log);
    }

    /**
     * Executes the template command.
     * 
     * @throws AppException
     */
    ProcessTask call() throws AppException;

}
