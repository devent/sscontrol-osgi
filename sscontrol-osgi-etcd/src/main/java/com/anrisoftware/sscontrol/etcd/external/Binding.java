package com.anrisoftware.sscontrol.etcd.external;

/**
 * <i>Binding</i>.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Binding {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface BindingFactory extends BindingService {
    }

    String getAddress();

    Integer getPort();

    String getScheme();
}
