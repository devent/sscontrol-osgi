package com.anrisoftware.sscontrol.types.misc.external;

import javax.annotation.concurrent.Immutable;

/**
 * User name and password credentials.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Immutable
public interface UserPassword {

    String getName();

    String getPassword();

    UserPassword changeName(String name);

    UserPassword changePassword(String password);

}
