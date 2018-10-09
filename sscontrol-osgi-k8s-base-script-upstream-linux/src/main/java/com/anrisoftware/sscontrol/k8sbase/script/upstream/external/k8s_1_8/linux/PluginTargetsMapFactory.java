package com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_8.linux;

import java.util.Map;

import com.anrisoftware.sscontrol.k8sbase.base.service.external.K8s;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Plugin;
import com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_8.linux.PluginTargetsMap;
import com.anrisoftware.sscontrol.types.host.external.HostServices;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface PluginTargetsMapFactory {

    PluginTargetsMap create(K8s service, HostServices repo,
            Map<String, Plugin> map);
}
