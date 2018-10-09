package com.anrisoftware.sscontrol.k8smaster.service.internal;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8smaster.service.external.AlwaysDenyAuthorization;
import com.anrisoftware.sscontrol.k8smaster.service.external.AuthorizationFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Blocks all requests.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class AlwaysDenyAuthorizationImpl implements AlwaysDenyAuthorization {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface AlwaysDenyAuthorizationImplFactory
            extends AuthorizationFactory {

    }

    @Inject
    AlwaysDenyAuthorizationImpl(@Assisted Map<String, Object> args) {
    }

    @Override
    public String getMode() {
        return "deny";
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
