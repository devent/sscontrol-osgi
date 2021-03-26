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
package com.anrisoftware.sscontrol.haproxy.script.haproxy_1_8.external

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.haproxy.service.external.Proxy
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Apply defaults to SSH proxy.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class SshApplyProxyDefaults implements ApplyProxyDefaults {

    @Override
    void applyDefaults(ScriptBase parent, Proxy proxy) {
        assertThat "Proxy backend cannot be null.", proxy.backend, notNullValue()
        if (proxy.frontend == null) {
            proxy.frontend name: getDefaultProxyFrontendName(parent), address: "*", port: getDefaultProxyFrontendPort(parent)
        }
        proxy.frontend.name = proxy.frontend.name == null ? getDefaultProxyFrontendName(parent) : proxy.frontend.name
        proxy.frontend.port = proxy.frontend.port == null ? getDefaultProxyFrontendPort(parent) : proxy.frontend.port
    }

    @Override
    String getName() {
        'ssh'
    }

    String getDefaultProxyFrontendName(ScriptBase parent) {
        parent.scriptProperties.default_proxy_ssh_frontend_name
    }

    int getDefaultProxyFrontendPort(ScriptBase parent) {
        parent.scriptNumberProperties.default_proxy_ssh_frontend_port
    }
}
