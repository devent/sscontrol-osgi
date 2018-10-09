package com.anrisoftware.sscontrol.flanneldocker.script.upstream.external


import static org.hamcrest.Matchers.*

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.flanneldocker.service.external.FlannelDocker
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.types.ssh.external.SshHost
import com.anrisoftware.sscontrol.types.ssh.external.TargetsListFactory

import groovy.util.logging.Slf4j

/**
 * Uses iperf to check the connectivity.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class AbstractIperfConnectionCheck extends ScriptBase {

    @Inject
    TargetsListFactory nodesFactory

    TemplateResource iperfCmdsResource

    @Inject
    def setTemplates(TemplatesFactory factory) {
        def t = factory.create 'FlannelDockerConnectionTestTemplates'
        this.iperfCmdsResource = t.getResource 'iperf_cmds'
    }

    def checkFlannel() {
        FlannelDocker service = this.service
        if (service.checkHost) {
            if (target != service.checkHost) {
                log.info 'Target!=check-host, nothing to do'
                return
            }
        }
        def iperfServers = []
        try {
            iperfServers = startServers()
            startClients iperfServers
        } finally {
            if (!iperfServers.empty) {
                stopServers(iperfServers)
            }
        }
    }

    List startServers() {
        log.info 'Stars iperf servers.'
        FlannelDocker service = this.service
        def list = []
        nodesFactory.create(service, scriptsRepository, "nodes", this).nodes.each {
            def p = shell target: it, privileged: true, outString: true, resource: iperfCmdsResource, name: 'startServer' call()
            list << parseServerOutput(it, p.out)
        }
        list
    }

    def stopServers(List servers) {
        log.info 'Stop iperf servers.'
        FlannelDocker service = this.service
        servers.each { Map map ->
            shell target: map.host, privileged: true, resource: iperfCmdsResource, name: 'stopServer', vars: [container: map] call()
        }
    }

    def startClients(List servers) {
        log.info 'Stars iperf clients.'
        FlannelDocker service = this.service
        servers.each { Map map ->
            shell privileged: true, resource: iperfCmdsResource, name: 'startClient', vars: [container: map] call()
        }
    }

    Map parseServerOutput(SshHost host, String out) {
        String[] s = out.split(";")
        def name = (s[0] =~ /Container: (.*)/)[0][1]
        def address = (s[1] =~ /Address: (.*)/)[0][1]
        [host: host, name: name, address: address]
    }

    @Override
    def getLog() {
        log
    }
}
