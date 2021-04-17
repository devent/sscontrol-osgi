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
package com.anrisoftware.sscontrol.groovy.script.external

import com.anrisoftware.sscontrol.types.host.external.HostService
import com.anrisoftware.sscontrol.types.host.external.HostServices

/**
 * Default implementation of ScriptBase.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractDefaultScriptBase extends ScriptBase {
    
    @Override
    Properties getDefaultProperties() {
    }

    boolean getCommandsQuiet() {
        false
    }

    @Override
    String getSystemName() {
        ''
    }

    @Override
    String getSystemVersion() {
        ''
    }

    @Override
    Boolean getArchiveIgnoreKey() {
        false
    }

    @Override
    HostService getService() {
    }

    @Override
    HostServices getScriptsRepository() {
    }

}
