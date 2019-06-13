/**
 * Copyright © 2016 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.command.shell.external.ssh;

import java.io.IOException;
import java.net.URI;

import com.anrisoftware.globalpom.exec.external.core.CommandExecException;

/**
 * 
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SetupSshKeyException extends CommandExecException {

    public SetupSshKeyException(IOException e, URI key) {
        super("Setup ssh key error", e);
        addContextValue("key", key);
    }

}
