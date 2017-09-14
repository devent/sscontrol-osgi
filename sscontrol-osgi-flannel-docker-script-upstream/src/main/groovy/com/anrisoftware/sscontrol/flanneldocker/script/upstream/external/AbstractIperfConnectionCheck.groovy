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
package com.anrisoftware.sscontrol.flanneldocker.script.upstream.external


import static org.hamcrest.Matchers.*

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.flanneldocker.service.external.FlannelDocker
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * Uses iperf to check the connectivity.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class AbstractIperfConnectionCheck extends ScriptBase {

    TemplateResource iperfCmdsResource

    @Inject
    def setTemplates(TemplatesFactory factory) {
        def t = factory.create 'FlannelDockerConnectionTestTemplates'
        this.iperfCmdsResource = t.getResource 'iperf_cmds'
    }

    def startServers() {
        log.info 'Stars iperf servers.'
        FlannelDocker service = this.service
        copy src: archive, hash: archiveHash, dest: "/tmp", direct: true, timeout: timeoutLong call()
        shell resource: iperfCmdsResource, name: 'installCmd' call()
    }

    @Override
    def getLog() {
        log
    }
}
