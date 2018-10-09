package com.anrisoftware.sscontrol.registry.docker.service.internal;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.registry.docker.service.external.UserCredentials;
import com.google.inject.assistedinject.Assisted;

/**
 * User credentials.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class UserCredentialsImpl extends AbstractCredentials
        implements UserCredentials {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface UserCredentialsImplFactory extends CredentialsFactory {

    }

    private final String user;

    private final String password;

    @Inject
    UserCredentialsImpl(@Assisted Map<String, Object> args) {
        super(args);
        Object v = args.get("name");
        this.user = v.toString();
        v = args.get("password");
        this.password = v.toString();
    }

    @Override
    public String getType() {
        return "user";
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return password;
    }

}
