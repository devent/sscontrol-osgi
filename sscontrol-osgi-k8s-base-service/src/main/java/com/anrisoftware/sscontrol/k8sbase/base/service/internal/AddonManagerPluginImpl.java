package com.anrisoftware.sscontrol.k8sbase.base.service.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8sbase.base.service.external.AddonManagerPlugin;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Addon-Manager</i> plugin.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class AddonManagerPluginImpl implements AddonManagerPlugin {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface AddonManagerPluginImplFactory extends PluginFactory {

    }

    @AssistedInject
    AddonManagerPluginImpl() {
        this(new HashMap<String, Object>());
    }

    @AssistedInject
    AddonManagerPluginImpl(@Assisted Map<String, Object> args) {
    }

    @Override
    public String getName() {
        return "addon-manager";
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
