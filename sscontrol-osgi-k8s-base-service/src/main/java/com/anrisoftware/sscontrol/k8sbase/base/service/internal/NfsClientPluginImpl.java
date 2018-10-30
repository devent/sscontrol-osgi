package com.anrisoftware.sscontrol.k8sbase.base.service.internal;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8sbase.base.service.external.NfsClientPlugin;

/**
 * <i>Nfs-Client</i> plugin.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class NfsClientPluginImpl implements NfsClientPlugin {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface NfsClientPluginImplFactory extends PluginFactory {

    }

    @Override
    public String getName() {
        return "nfs-client";
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
