package com.anrisoftware.sscontrol.haproxy.script.haproxy_1_8.external

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.haproxy.service.external.Proxy
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Apply defaults to HTTPS proxy.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class HttpsApplyProxyDefaults implements ApplyProxyDefaults {

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
        'https'
    }

    String getDefaultProxyFrontendName(ScriptBase parent) {
        parent.scriptProperties.default_proxy_https_frontend_name
    }

    int getDefaultProxyFrontendPort(ScriptBase parent) {
        parent.scriptNumberProperties.default_proxy_https_frontend_port
    }
}
