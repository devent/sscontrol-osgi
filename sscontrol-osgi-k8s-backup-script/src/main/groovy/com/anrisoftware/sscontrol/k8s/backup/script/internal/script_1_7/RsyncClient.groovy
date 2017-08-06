package com.anrisoftware.sscontrol.k8s.backup.script.internal.script_1_7

import javax.inject.Inject

import org.stringtemplate.v4.ST

import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8s.backup.service.external.Backup
import com.google.inject.assistedinject.Assisted

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class RsyncClient {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    interface RsyncClientFactory {

        RsyncClient create(ScriptBase script, Backup service)
    }

    @Inject
    @Assisted
    ScriptBase script

    @Inject
    @Assisted
    Backup service

    def rsyncCmd

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create('RsyncClient_Templates')
        this.rsyncCmd = templates.getResource("rsync_cmd")
    }

    void start(Map args) {
        def vars = [:]
        vars.rsync = [:]
        vars.rsync.user = "root"
        vars.rsync.host = service.cluster.host
        vars.rsync.port = args.port
        vars.rsync.key = script.createTmpFile()
        vars.rsync.arguments = service.destination.arguments
        vars.config = parseConfig service.client.config, vars.rsync
        vars.path = "/data"
        vars.proxy = getProxy()
        vars.dir = service.destination.dir
        script.copyResource src: service.client.key, dest: vars.rsync.key
        try {
            def a = [:]
            a.timeout = service.client.timeout
            a.resource = rsyncCmd
            a.name = "rsyncCmd"
            a.vars = vars
            script.shell a call()
        } finally {
            script.deleteTmpFile vars.rsync.key
        }
    }

    String parseConfig(String str, Map rsync) {
        if (!service.client.config) {
            return null
        }
        new ST(str).add("rsync", rsync).render()
    }

    Map getProxy() {
        if (!service.client.proxy) {
            return null
        }
        def proxy = [:]
        proxy.user = service.cluster.cluster.target.user
        if (service.cluster.cluster.target.socket) {
            proxy.socket = service.cluster.cluster.target.socket
        }
        return proxy
    }
}
