/*-
 * #%L
 * sscontrol-osgi - collectd-script-collectd-5-7
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.anrisoftware.sscontrol.nfs.script.nfs_1_3.external

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.nfs.service.external.Nfs

import groovy.util.logging.Slf4j

/**
 * Nfs 1.3.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Nfs_1_3 extends ScriptBase {

    TemplateResource exportsTemplate

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create('Nfs_1_3_Templates')
        this.exportsTemplate = templates.getResource('exports')
    }

    /**
     * Deploys the Nfs exports to {@code /etc/exports}.
     */
    def deployExports() {
        Nfs service = this.service
        template privileged: true, resource: exportsTemplate, name: 'exportsConfig', vars: [:], dest: configFile call()
    }

    @Override
    def getLog() {
        log
    }
}
