package com.anrisoftware.sscontrol.repo.git.service.external;

/**
 * <i>Git</i> checkout.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Checkout {

    String getBranch();

    String getTag();

    String getCommit();
}
