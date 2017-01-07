package com.anrisoftware.sscontrol.k8smaster.external;

/**
 * <i>Etcd</i> plugin.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface EtcdPlugin extends Plugin {

    String getTarget();
}
