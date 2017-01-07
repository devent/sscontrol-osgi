package com.anrisoftware.sscontrol.k8smaster.external;

/**
 * <i>Flannel</i> plugin.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface FlannelPlugin extends Plugin {

    String getRange();
}
