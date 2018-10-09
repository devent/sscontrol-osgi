package com.anrisoftware.sscontrol.types.misc.external;

/**
 * Creates the user with a password.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface UserPasswordService {

    UserPassword create(String name, String password);
}
