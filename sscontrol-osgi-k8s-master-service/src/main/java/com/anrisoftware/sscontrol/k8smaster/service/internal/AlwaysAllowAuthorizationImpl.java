package com.anrisoftware.sscontrol.k8smaster.service.internal;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8smaster.service.external.AlwaysAllowAuthorization;
import com.anrisoftware.sscontrol.k8smaster.service.external.AuthorizationFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Allows all requests.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class AlwaysAllowAuthorizationImpl implements AlwaysAllowAuthorization {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface AlwaysAllowAuthorizationImplFactory
            extends AuthorizationFactory {

    }

    @Inject
    AlwaysAllowAuthorizationImpl(@Assisted Map<String, Object> args) {
    }

    @Override
    public String getMode() {
        return "allow";
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
