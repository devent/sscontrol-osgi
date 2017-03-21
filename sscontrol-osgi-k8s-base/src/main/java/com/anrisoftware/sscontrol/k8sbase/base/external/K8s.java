package com.anrisoftware.sscontrol.k8sbase.base.external;

import java.util.Map;

import com.anrisoftware.sscontrol.types.external.HostService;

/**
 * <i>K8s</i> service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface K8s extends HostService {

    Map<String, Plugin> getPlugins();

}
