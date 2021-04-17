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
package com.anrisoftware.sscontrol.docker.service.internal;

import static com.anrisoftware.sscontrol.docker.service.internal.DockerImplLogger.m.cgroupAdded;
import static com.anrisoftware.sscontrol.docker.service.internal.DockerImplLogger.m.registrySet;
import static com.anrisoftware.sscontrol.docker.service.internal.DockerImplLogger.m.targetsAdded;

import java.util.List;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.docker.service.external.LoggingDriver;
import com.anrisoftware.sscontrol.docker.service.external.Registry;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;

/**
 * Logging for {@link DockerImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class DockerImplLogger extends AbstractLogger {

    enum m {

        cgroupAdded("Cgroup {} added to {}"),

        registrySet("Registry {} set for {}"),

        targetsAdded("Targets {} added to {}"),

        loggingDriverSet("Logging driver {} set for {}");

        private String name;

        private m(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link DockerImpl}.
     */
    public DockerImplLogger() {
        super(DockerImpl.class);
    }

    void cgroupAdded(DockerImpl docker, String cgroup) {
        debug(cgroupAdded, cgroup, docker);
    }

    void registrySet(DockerImpl docker, Registry registry) {
        debug(registrySet, registry, docker);
    }

    void targetsAdded(DockerImpl docker, List<TargetHost> list) {
        debug(targetsAdded, list, docker);
    }

    void loggingDriverSet(DockerImpl docker, LoggingDriver driver) {
        debug(m.loggingDriverSet, driver, docker);
    }
}
