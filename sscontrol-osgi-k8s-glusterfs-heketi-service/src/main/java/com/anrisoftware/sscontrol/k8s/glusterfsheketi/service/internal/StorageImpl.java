package com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.external.Storage;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Glusterfs-Heketi storage.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class StorageImpl implements Storage {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface StorageImplFactory {

        Storage create(Map<String, Object> args);

        Storage create();

    }

    private String name;

    private String restAddress;

    private String restUser;

    private String restKey;

    private Boolean isDefault;

    @AssistedInject
    StorageImpl() {
        this(new HashMap<String, Object>());
    }

    @AssistedInject
    StorageImpl(@Assisted Map<String, Object> args) {
        Object v = args.get("name");
        if (v != null) {
            this.name = v.toString();
        }
        v = args.get("address");
        if (v != null) {
            this.restAddress = v.toString();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setRestAddress(String restAddress) {
        this.restAddress = restAddress;
    }

    @Override
    public String getRestAddress() {
        return restAddress;
    }

    public void setRestUser(String restUser) {
        this.restUser = restUser;
    }

    @Override
    public String getRestUser() {
        return restUser;
    }

    public void setRestKey(String restKey) {
        this.restKey = restKey;
    }

    @Override
    public String getRestKey() {
        return restKey;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public Boolean getIsDefault() {
        return isDefault;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
