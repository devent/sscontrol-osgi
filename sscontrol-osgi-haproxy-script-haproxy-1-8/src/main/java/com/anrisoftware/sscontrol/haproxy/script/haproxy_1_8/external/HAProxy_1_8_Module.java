package com.anrisoftware.sscontrol.haproxy.script.haproxy_1_8.external;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class HAProxy_1_8_Module extends AbstractModule {

    @Override
    protected void configure() {
        bindApplyProxyDefaults();
    }

    private void bindApplyProxyDefaults() {
        MapBinder<String, ApplyProxyDefaults> map = newMapBinder(binder(), String.class, ApplyProxyDefaults.class);
        map.addBinding("http").to(HttpApplyProxyDefaults.class);
        map.addBinding("https").to(HttpsApplyProxyDefaults.class);
        map.addBinding("ssh").to(SshApplyProxyDefaults.class);
    }

}
