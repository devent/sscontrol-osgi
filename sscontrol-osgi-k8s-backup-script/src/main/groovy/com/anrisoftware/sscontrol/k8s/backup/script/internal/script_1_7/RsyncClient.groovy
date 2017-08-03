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
        vars.rsync.user = "rsync"
        vars.rsync.host = service.cluster.cluster.target.host
        vars.rsync.port = args.port
        vars.rsync.key = script.createTmpFile()
        vars.config = parseConfig service.client.config, vars.rsync
        vars.path = "/data"
        script.copyResource src: service.client.key, dest: vars.rsync.key
        try {
            script.shell resource: rsyncCmd, name: "rsyncCmd", vars: vars call()
        } finally {
            script.deleteTmpFile vars.rsync.key
        }
    }

    String parseConfig(String str, Map rsync) {
        new ST(str).add("rsync", rsync).render()
    }
}
