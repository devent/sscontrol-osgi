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
package com.anrisoftware.sscontrol.etcd.script.upstream.external

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.etcd.service.external.Etcd
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.st.miscrenderers.external.NumberTrueRenderer

import groovy.util.logging.Slf4j

/**
 * Checks the <i>Etcd</i> 3.x cluster.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Etcd_3_x_Check extends ScriptBase {

    TemplateResource checkTemplate

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory, NumberTrueRenderer numberTrueRenderer) {
        def templates = templatesFactory.create('Etcd_3_x_Check_Templates')
        this.checkTemplate = templates.getResource('etcd_check')
    }

    @Override
    Object run() {
        Etcd service = this.service
        if (service.checkHost) {
            if (target != service.checkHost) {
                log.info 'Target!=check-host, nothing to do'
                return
            }
        }
        shell privileged: true, resource: checkTemplate, name: "checkEtcd", vars: [:] call()
    }

    File getEtcdctlVariablesFile() {
        getFileProperty "etcdctl_variables_file"
    }

    @Override
    def getLog() {
        log
    }
}
