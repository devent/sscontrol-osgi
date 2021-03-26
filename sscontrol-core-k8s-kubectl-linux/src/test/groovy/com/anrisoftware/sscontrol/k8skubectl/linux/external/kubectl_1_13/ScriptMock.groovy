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
package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_13

import java.util.concurrent.ExecutorService

import org.joda.time.Duration

import com.anrisoftware.sscontrol.types.host.external.HostService
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.host.external.TargetHost

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class ScriptMock implements HostServiceScript {

    @Override
    Object run() {
    }

    Duration getKubectlTimeout() {
        Duration.standardMinutes 2
    }

    @Override
    String getSystemName() {
    }

    @Override
    String getSystemVersion() {
    }

    @Override
    Object getLog() {
    }

    @Override
    HostServiceProperties getProperties() {
    }

    @Override
    <T extends ExecutorService> T getThreads() {
    }

    @Override
    Properties getDefaultProperties() {
    }

    @Override
    HostService getService() {
    }

    @Override
    HostServices getScriptsRepository() {
    }

    @Override
    TargetHost getTarget() {
    }
}
