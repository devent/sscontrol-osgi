/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
