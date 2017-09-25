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
package com.anrisoftware.sscontrol.utils.ufw.linux.external

import static org.apache.commons.io.FilenameUtils.*
import static org.apache.commons.lang3.Validate.*
import static org.hamcrest.Matchers.*

import com.anrisoftware.globalpom.exec.external.core.ProcessTask
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 * Ufw utilities.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class UfwUtils {

    final HostServiceScript script

    protected UfwUtils(HostServiceScript script) {
        this.script = script
    }

    /**
     * Returns the default {@link Properties} for the service.
     */
    Properties getDefaultProperties() {
        script.defaultProperties
    }

    boolean isUfwActive() {
        ProcessTask ret = script.shell privileged: true, outString: true, exitCodes: [0, 1] as int[], "which ufw>/dev/null && ufw status" call()
        return ret.exitValue == 0 && (ret.out =~ /Status: active.*/)
    }

    /**
     * Returns a list of tcp ports from the property {@code tcp_ports}.
     * Translates port range to Ufw syntax.
     */
    List getTcpPorts(List ports) {
        def list = ports
        list.inject([]) { l, String v ->
            l << v.replaceAll('-', ':')
        }
    }
}
