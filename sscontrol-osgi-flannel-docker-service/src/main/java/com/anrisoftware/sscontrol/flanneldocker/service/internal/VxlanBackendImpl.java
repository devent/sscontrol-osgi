package com.anrisoftware.sscontrol.flanneldocker.service.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.flanneldocker.service.external.Backend;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Vxlan</i> backend.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class VxlanBackendImpl implements Backend {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface VxlanBackendImplFactory extends BackendFactory {

    }

    @AssistedInject
    VxlanBackendImpl() {
        this(new HashMap<String, Object>());
    }

    @AssistedInject
    VxlanBackendImpl(@Assisted Map<String, Object> args) {
        parseArgs(args);
    }

    @Override
    public String getType() {
        return "vxlan";
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("type", getType()).toString();
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("address");
        if (v != null) {
        }
    }

}
