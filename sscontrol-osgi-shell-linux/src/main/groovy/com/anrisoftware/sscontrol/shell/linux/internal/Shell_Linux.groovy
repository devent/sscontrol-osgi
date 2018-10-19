/*-
 * #%L
 * sscontrol-osgi - shell-linux
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.anrisoftware.sscontrol.shell.linux.internal

import static com.anrisoftware.sscontrol.shell.linux.internal.Shell_Linux_Service.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.shell.external.Shell

import groovy.util.logging.Slf4j

/**
 * Runs scripts on GNU/Linux systems.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Shell_Linux extends ScriptBase {

    @Inject
    Shell_Linux_Properties linuxPropertiesProvider

    @Override
    ContextProperties getDefaultProperties() {
        linuxPropertiesProvider.get()
    }

    @Override
    def run() {
        Shell service = this.service
        service.scripts.each {
            log.trace 'Run script {}', it
            shell it.vars call()
        }
    }

    @Override
    def getLog() {
        log
    }

    @Override
    String getSystemName() {
        SYSTEM_NAME
    }

    @Override
    String getSystemVersion() {
        SYSTEM_VERSION
    }
}
