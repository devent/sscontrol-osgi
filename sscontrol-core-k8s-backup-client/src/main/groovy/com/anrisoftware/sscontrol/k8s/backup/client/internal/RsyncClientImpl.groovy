/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.k8s.backup.client.internal

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import javax.inject.Inject

import org.stringtemplate.v4.ST

import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8s.backup.client.external.Client
import com.anrisoftware.sscontrol.k8s.backup.client.external.Destination
import com.anrisoftware.sscontrol.k8s.backup.client.external.RsyncClient
import com.anrisoftware.sscontrol.k8s.backup.client.external.Service
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost
import com.google.inject.assistedinject.Assisted

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class RsyncClientImpl implements RsyncClient {

    private final ScriptBase script

    private final Service service

    private final Client client

    private final ClusterHost cluster

    private final Destination destination

    def rsyncCmd

    @Inject
    RsyncClientImpl(
    @Assisted ScriptBase script,
    @Assisted Service service,
    @Assisted ClusterHost cluster,
    @Assisted Client client,
    @Assisted Destination destination) {
        this.script = script
        this.service = service
        this.client = client
        this.cluster = cluster
        this.destination = destination
    }

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create('RsyncClient_Templates')
        this.rsyncCmd = templates.getResource("rsync_cmd")
    }

    @Override
    void start(Map args) {
        assertThat "args.backup!=null", args.backup, notNullValue()
        assertThat "args.path!=null", args.path, notNullValue()
        assertThat "args.dir!=null", args.dir, notNullValue()
        assertThat "args.port!=null", args.port, notNullValue()
        def vars = new HashMap(args)
        vars.rsync = [:]
        vars.rsync.backup = args.backup
        vars.rsync.user = "root"
        vars.rsync.host = cluster.host
        vars.rsync.port = args.port
        vars.rsync.key = script.createTmpFile()
        vars.rsync.arguments = destination.arguments
        vars.config = parseConfig client.config, vars.rsync
        vars.path = args.path
        vars.proxy = getProxy()
        vars.dir = args.dir
        script.copyResource src: client.key, dest: vars.rsync.key
        try {
            def a = [:]
            a.timeout = client.timeout
            a.resource = rsyncCmd
            a.name = "rsyncCmd"
            a.vars = vars
            script.shell a call()
        } finally {
            script.deleteTmpFile vars.rsync.key
        }
    }

    String parseConfig(String str, Map rsync) {
        if (!client.config) {
            return null
        }
        new ST(str).add("rsync", rsync).render()
    }

    Map getProxy() {
        if (!client.proxy) {
            return null
        }
        def proxy = [:]
        proxy.user = cluster.cluster.target.user
        if (cluster.cluster.target.socket) {
            proxy.socket = cluster.cluster.target.socket
        }
        return proxy
    }
}
