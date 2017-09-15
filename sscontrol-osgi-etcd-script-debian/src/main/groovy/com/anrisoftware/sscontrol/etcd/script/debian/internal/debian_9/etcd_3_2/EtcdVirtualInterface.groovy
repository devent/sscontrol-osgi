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
package com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_9.etcd_3_2

import static com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_9.etcd_3_2.EtcdDebianService.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.etcd.service.external.Etcd
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.st.miscrenderers.external.NumberTrueRenderer
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtilsFactory

import groovy.util.logging.Slf4j

/**
 * Configures a virtual interface to bind to.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class EtcdVirtualInterface extends ScriptBase {

    @Inject
    EtcdDebianProperties debianPropertiesProvider

    TemplateResource networkConfigTemplate

    SystemdUtils systemd

    @Inject
    void setSystemd(SystemdUtilsFactory factory) {
        this.systemd = factory.create this
    }

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory, NumberTrueRenderer numberTrueRenderer) {
        def templates = templatesFactory.create('Etcd_3_2_Debian_9_VirtualInterfaceTemplates')
        this.networkConfigTemplate = templates.getResource('network_config')
    }

    @Override
    Object run() {
        Etcd service = this.service
        if (!service.bindings.find({ it.network != null })) {
            return
        }
        log.info 'Update network config'
        updateNetworkConfig()
        service.bindings.each {
            shell privileged: true, "ifdown ${it.network}; ifup ${it.network}" call()
        }
    }

    def updateNetworkConfig() {
        interfaceBinding.each { String string ->
            replace privileged: true, dest: networkInterfacesFile with {
                line "s/(?m)^${string}/${string}/"
                it
            }()
        }
    }

    List<String> getInterfaceBinding() {
        Etcd service = this.service
        service.bindings.inject([]) { list, b ->
            networkConfigTemplate.invalidate()
            def s = networkConfigTemplate.getText "networkConfig", "parent", this, "vars",
                    ["network": b.network, "address": b.address.host]
            list << s
        }
    }

    File getNetworkInterfacesFile() {
        getFileProperty "network_interfaces_file"
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
