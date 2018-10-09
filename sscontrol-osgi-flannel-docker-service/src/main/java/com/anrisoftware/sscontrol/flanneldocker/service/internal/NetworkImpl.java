package com.anrisoftware.sscontrol.flanneldocker.service.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.flanneldocker.service.external.Network;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Flannel-Docker</i> Etcd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class NetworkImpl implements Network {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface NetworkImplFactory {

        Network create();

        Network create(Map<String, Object> args);

    }

    private String address;

    @AssistedInject
    NetworkImpl() {
        this(new HashMap<String, Object>());
    }

    @AssistedInject
    NetworkImpl(@Assisted Map<String, Object> args) {
        parseArgs(args);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("address", getAddress())
                .toString();
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("address");
        if (v != null) {
            setAddress(v.toString());
        }
    }

}
