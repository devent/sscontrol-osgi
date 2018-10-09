package com.anrisoftware.sscontrol.flanneldocker.script.debian.internal.flanneldocker_0_9.debian_9

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.flanneldocker.service.external.FlannelDocker
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.types.ssh.external.TargetsAddressListFactory
import com.anrisoftware.sscontrol.types.ssh.external.TargetsListFactory
import com.anrisoftware.sscontrol.utils.ufw.linux.external.UfwLinuxUtilsFactory
import com.anrisoftware.sscontrol.utils.ufw.linux.external.UfwUtils

import groovy.util.logging.Slf4j

/**
 * Configures the Ufw firewall.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class FlannelDockerUfw extends ScriptBase {

    @Inject
    FlannelDockerDebianProperties debianPropertiesProvider

    @Inject
    TargetsAddressListFactory addressesFactory

    @Inject
    TargetsListFactory nodesFactory

    UfwUtils ufw

    @Inject
    void setUfwUtilsFactory(UfwLinuxUtilsFactory factory) {
        this.ufw = factory.create this
    }

    @Override
    Object run() {
        FlannelDocker service = this.service
        if (!ufw.ufwActive) {
            log.debug 'No Ufw available.'
            return
        }
        if (service.nodes.empty) {
            log.debug 'No nodes available.'
            return
        }
        updateFirewall()
    }

    def updateFirewall() {
        FlannelDocker service = this.service
        ufw.allowPortsOnNodes nodes, nodesAddresses, ["8472"], "udp", this
    }

    List getNodes() {
        FlannelDocker service = this.service
        nodesFactory.create(service, scriptsRepository, "nodes", this).nodes
    }

    List getNodesAddresses() {
        FlannelDocker service = this.service
        addressesFactory.create(service, scriptsRepository, "nodes", this).nodes
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
