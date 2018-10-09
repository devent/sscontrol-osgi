package com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal;

import java.util.HashMap;
import java.util.Map;

import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.external.Admin;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Admin service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class AdminImpl extends AbstractUserImpl implements Admin {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface AdminImplFactory {

        Admin create(Map<String, Object> args);

        Admin create();

    }

    @AssistedInject
    AdminImpl() {
        super(new HashMap<String, Object>());
    }

    @AssistedInject
    AdminImpl(@Assisted Map<String, Object> args) {
        super(args);
    }

}
