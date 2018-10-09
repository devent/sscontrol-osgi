package com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_8.linux

import javax.inject.Inject

import com.anrisoftware.sscontrol.k8sbase.base.service.external.K8s
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Plugin
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.google.inject.assistedinject.Assisted

/**
 * Returns a map with the host, protocol and port of the plugin target.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class PluginTargetsMap extends HashMap {

    K8s service

    HostServices repo

    @Inject
    PluginTargetsMap(
    @Assisted K8s service,
    @Assisted HostServices repo,
    @Assisted Map<String, Plugin> map) {
        super(map)
        this.service = service
        this.repo = repo
    }

    Object get(Object key) {
        Plugin plugin = service.plugins[key]
        if (!plugin) {
            return []
        }
        if (plugin.endpoints) {
            return getEndpoints(plugin, key)
        }
    }

    private List getEndpoints(Plugin plugin, String key) {
        plugin.endpoints.inject([]) { result, e ->
            if (e instanceof String) {
                if (new URI(e).scheme) {
                    result << endpointString(plugin, e)
                } else {
                    endpointTargets(result, plugin, e)
                }
            }
            result
        }
    }

    def endpointString(Plugin plugin, String endpoint) {
        def uri = new URI(endpoint)
        def host = uri.host ? uri.host : uri.path
        def proto = uri.scheme
        def port = uri.port != -1 ? uri.port : plugin.port
        [host: host, protocol: proto, port: port]
    }

    def endpointTargets(List list, Plugin plugin, String target) {
        repo.targets(target).each {
            list << [host: it.host, protocol: plugin.protocol, port: plugin.port]
        }
    }
}
