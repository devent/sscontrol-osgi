package com.anrisoftware.sscontrol.k8sbase.base.service.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8sbase.base.service.external.CanalPlugin;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Canal</i> plugin.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class CanalPluginImpl implements CanalPlugin {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface CanalPluginImplFactory extends PluginFactory {

    }

    private final CanalPluginImplLogger log;

    private String iface;

    @AssistedInject
    CanalPluginImpl(CanalPluginImplLogger log) {
        this(log, new HashMap<String, Object>());
    }

    @AssistedInject
    CanalPluginImpl(CanalPluginImplLogger log,
            @Assisted Map<String, Object> args) {
        this.log = log;
        parseArgs(args);
    }

    /**
     * <pre>
     * iface name: "enp0s8"
     * </pre>
     */
    public void iface(Map<String, Object> args) {
        Object v = args.get("name");
        assertThat("name!=null", (String) v, not(isEmptyOrNullString()));
        if (v != null) {
            setIface(v.toString());
        }
    }

    @Override
    public String getName() {
        return "canal";
    }

    public void setIface(String iface) {
        this.iface = iface;
        log.ifaceSet(this, iface);
    }

    @Override
    public String getIface() {
        return iface;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        parseIface(args);
    }

    private void parseIface(Map<String, Object> args) {
        Object v = args.get("iface");
        if (v != null) {
            setIface(v.toString());
        }
    }

}
