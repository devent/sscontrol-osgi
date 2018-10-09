package com.anrisoftware.sscontrol.types.misc.internal;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.types.misc.external.UserPassword;
import com.anrisoftware.sscontrol.types.misc.external.UserPasswordService;

/**
 * Creates the user with a password.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(UserPasswordService.class)
public class UserPasswordServiceImpl implements UserPasswordService {

    @Override
    public UserPassword create(String name, String password) {
        return new UserPasswordImpl(name, password);
    }

}
