package com.anrisoftware.sscontrol.types.misc.external;

/**
 * Binding host and port service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface BindingHostService {

    BindingHost create();

    BindingHost create(BindingHost binding);
}
