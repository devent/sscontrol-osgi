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
package com.anrisoftware.sscontrol.shell.external;

import java.util.Map;

import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.threads.external.core.Threads;
import com.anrisoftware.sscontrol.types.external.app.AppException;
import com.anrisoftware.sscontrol.types.external.ssh.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * Scp command.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Scp {

    /**
     * Factory to create the scp command.
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ScpFactory {

        Scp create(@Assisted Map<String, Object> args, @Assisted SshHost host,
                @Assisted("parent") Object parent, @Assisted Threads threads,
                @Assisted("log") Object log);
    }

    /**
     * Executes the scp command.
     * 
     * @throws AppException
     */
    ProcessTask call() throws AppException;

}
