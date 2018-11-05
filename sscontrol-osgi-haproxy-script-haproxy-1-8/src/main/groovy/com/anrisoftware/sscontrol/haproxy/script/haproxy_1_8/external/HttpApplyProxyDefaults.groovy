package com.anrisoftware.sscontrol.haproxy.script.haproxy_1_8.external

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.haproxy.service.external.Proxy
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Apply defaults to HTTP proxy.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class HttpApplyProxyDefaults implements ApplyProxyDefaults {

    @Override
    void applyDefaults(ScriptBase parent, Proxy proxy) {
        assertThat "Proxy backend cannot be null.", proxy.backend, notNullValue()
        proxy.frontend == null ? {
            proxy.frontend name: "node-http", address: "*", port: getDefaultProxyFrontendPort(parent)
        } : {
            proxy.frontend.port = proxy.frontend.port == null ? getDefaultProxyFrontendPort(parent) : proxy.frontend.port
        }
    }

    @Override
    String getName() {
        'http'
    }
    
    int getDefaultProxyFrontendPort(ScriptBase parent) {
        parent.scriptNumberProperties.default_proxy_http_frontend_port
    }
}
