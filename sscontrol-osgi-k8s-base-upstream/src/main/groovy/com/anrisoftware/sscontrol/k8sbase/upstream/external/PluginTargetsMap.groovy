package com.anrisoftware.sscontrol.k8sbase.upstream.external

import javax.inject.Inject

import com.anrisoftware.sscontrol.k8sbase.base.external.K8s
import com.anrisoftware.sscontrol.k8sbase.base.external.Plugin
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.google.inject.assistedinject.Assisted

/**
 * Returns a map with the host, protocol and port of the plugin target.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class PluginTargetsMap extends HashMap {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    interface PluginTargetsMapFactory {

        PluginTargetsMap create(K8s service, HostServices repo, Map<String, Plugin> map)
    }

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
        if (plugin.targets) {
            return getSshTargets(plugin, key)
        }
        if (plugin.addresses) {
            return getAddresses(plugin, key)
        }
    }

    private List getSshTargets(Plugin plugin, String key) {
        def repo = repo
        plugin.targets.inject([]) { result, target ->
            repo.targets(target).each {
                result << [host: it.host, protocol: plugin.protocol, port: plugin.port]
            }
            result
        }
    }

    private List getAddresses(Plugin plugin, String key) {
        plugin.addresses.inject([]) { result, address ->
            def uri = new URI(address)
            def host = uri.host ? uri.host : uri.path
            def proto = uri.scheme ? uri.scheme : plugin.protocol
            def port = uri.port != -1 ? uri.port : plugin.port
            result << [host: host, protocol: proto, port: port]
            result
        }
    }
}
