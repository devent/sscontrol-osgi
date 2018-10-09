package com.anrisoftware.sscontrol.repo.git.service.external;

import com.anrisoftware.sscontrol.types.repo.external.Repo;

/**
 * <i>Git</i> code repository service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface GitRepo extends Repo {

    Remote getRemote();

    /**
     * The commit that should be checkout.
     */
    Checkout getCheckout();

    Credentials getCredentials();

}
