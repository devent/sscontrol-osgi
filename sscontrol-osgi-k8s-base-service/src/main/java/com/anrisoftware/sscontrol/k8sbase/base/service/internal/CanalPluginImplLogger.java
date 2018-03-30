package com.anrisoftware.sscontrol.k8sbase.base.service.internal;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link CanalPluginImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class CanalPluginImplLogger extends AbstractLogger {

    enum m {

        ifaceSet("Iface {} set for {}");

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
     * Sets the context of the logger to {@link CanalPluginImpl}.
     */
    public CanalPluginImplLogger() {
        super(CanalPluginImpl.class);
    }

    void ifaceSet(CanalPluginImpl canal, String iface) {
        debug(m.ifaceSet, iface, canal);
    }

}
