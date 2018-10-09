package com.anrisoftware.sscontrol.k8smaster.service.internal;

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.core.resources.ToURIFactory;
import com.anrisoftware.sscontrol.k8smaster.service.external.AuthenticationFactory;
import com.anrisoftware.sscontrol.k8smaster.service.external.BasicAuthentication;
import com.google.inject.assistedinject.Assisted;

/**
 * Static Token File.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class BasicAuthenticationImpl implements BasicAuthentication {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface BasicAuthenticationImplFactory
            extends AuthenticationFactory {

    }

    private URI file;

    private String tokens;

    @Inject
    BasicAuthenticationImpl(ToURIFactory touri,
            @Assisted Map<String, Object> args) {
        Object v = args.get("file");
        if (v != null) {
            this.file = touri.create(v).convert();
        } else {
            this.file = null;
        }
        v = args.get("tokens");
        if (v != null) {
            this.tokens = v.toString();
        } else {
            this.tokens = null;
        }
    }

    @Override
    public String getType() {
        return "basic";
    }

    @Override
    public URI getFile() {
        return file;
    }

    @Override
    public String getTokens() {
        return tokens;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
