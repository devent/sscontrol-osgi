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
package com.anrisoftware.sscontrol.nfs.script.nfs_1_3.external

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.nfs.service.external.Export
import com.anrisoftware.sscontrol.nfs.service.external.Host
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
     * Setups the default options for hosts without options.
     */
    def setupDefaultOptions() {
        Nfs service = this.service
        service.exports.each { Export export ->
            def hosts = export.hosts.findAll { Host host -> host.options == null }
            hosts.each { Host host ->
                if (host.options == null) {
                    host.options = defaultExportsOptions
                }
            }
        }
    }

    /**
     * Deploys the Nfs exports to {@code /etc/exports}.
     */
    def deployExports() {
        Nfs service = this.service
        template privileged: true, resource: exportsTemplate, name: 'exportsConfig', vars: [:], dest: configFile call()
    }

    /**
     * Creates the export directories.
     */
    def createExports() {
        Nfs service = this.service
        service.exports.each { Export export ->
            shell privileged: true, """
mkdir -p ${export.dir}
chown ${nfsnobodyUser}.${nfsnobodyGroup} -R ${export.dir}
chmod ${nfsExportsPermissions} ${export.dir}
""" call()
        }
    }

    /**
     * Returns the default options for exports, for example {@code rw,sync,no_root_squash}.
     */
    String getDefaultExportsOptions() {
        getScriptProperty "default_exports_options"
    }

    /**
     * Returns the nfs-nobody user.
     */
    String getNfsnobodyUser() {
        getScriptProperty "nfsnobody_user"
    }

    /**
     * Returns the nfs-nobody group.
     */
    String getNfsnobodyGroup() {
        getScriptProperty "nfsnobody_group"
    }

    /**
     * Returns the Nfs export directories permissions to set.
     */
    String getNfsExportsPermissions() {
        getScriptProperty "nfs_exports_permissions"
    }

    @Override
    def getLog() {
        log
    }
}
