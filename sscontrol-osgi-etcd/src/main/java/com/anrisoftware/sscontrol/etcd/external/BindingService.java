package com.anrisoftware.sscontrol.etcd.external;

import java.util.Map;

/**
 * <i>Binding</i> service.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface BindingService {

    Binding create();

    Binding create(Map<String, Object> args);

}
