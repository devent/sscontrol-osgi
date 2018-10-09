package com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal;

import java.util.HashMap;
import java.util.Map;

import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.external.User;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * User service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class UserImpl extends AbstractUserImpl {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface UserImplFactory {

        User create(Map<String, Object> args);

        User create();

    }

    @AssistedInject
    UserImpl() {
        super(new HashMap<String, Object>());
    }

    @AssistedInject
    UserImpl(@Assisted Map<String, Object> args) {
        super(args);
    }

}
