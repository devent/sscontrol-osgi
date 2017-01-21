package com.anrisoftware.sscontrol.etcd.external;

/**
 * <i>Binding</i>.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Binding {

    String getAddress();

    Integer getPort();

    String getScheme();
}
