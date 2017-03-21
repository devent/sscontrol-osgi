package com.anrisoftware.sscontrol.k8snode.upstream.external

import javax.inject.Inject

import com.anrisoftware.sscontrol.k8smaster.external.K8sMaster
import com.anrisoftware.sscontrol.k8smaster.external.Plugin
import com.anrisoftware.sscontrol.types.external.HostServices
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

        PluginTargetsMap create(K8sMaster service, HostServices repo, Map<String, Plugin> map)
    }

    K8sMaster service

    HostServices repo

    @Inject
    PluginTargetsMap(
    @Assisted K8sMaster service,
    @Assisted HostServices repo,
    @Assisted Map<String, Plugin> map) {
        super(map)
        this.service = service
        this.repo = repo
    }

    Object get(Object key) {
        Plugin plugin = service.plugins[key]
        if (plugin.target) {
            return getSshTarget(plugin, key)
        }
        if (plugin.address) {
            return getAddress(plugin, key)
        }
    }

    private List getSshTarget(Plugin plugin, String key) {
        def targets = repo.targets(plugin.target)
        def list = []
        targets.host.each {
            list << [host: it, protocol: plugin.protocol, port: plugin.port]
        }
        return list
    }

    private List getAddress(Plugin plugin, String key) {
        def address = new URI(plugin.address)
        def host = address.host ? address.host : address.path
        def proto = address.scheme ? address.scheme : plugin.protocol
        def port = address.port != -1 ? address.port : plugin.port
        def list = []
        list << [host: host, protocol: proto, port: port]
        return list
    }
}
