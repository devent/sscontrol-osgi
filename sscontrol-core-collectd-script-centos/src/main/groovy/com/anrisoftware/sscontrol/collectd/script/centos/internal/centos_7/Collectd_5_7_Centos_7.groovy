/**
 * Copyright © 2016 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.collectd.script.centos.internal.centos_7

import static com.anrisoftware.sscontrol.collectd.script.centos.internal.centos_7.Collectd_Centos_7_Service.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.collectd.script.collect_5_8.external.Collectd_5_8

import groovy.util.logging.Slf4j

/**
 * Collectd 5.7. for CentOS 7.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Collectd_5_7_Centos_7 extends Collectd_5_8 {

    @Inject
    Collectd_Centos_7_Properties propertiesProvider

    TemplateResource collectdRulesTemplate

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create('Collectd_5_7_Centos_7_Templates')
        this.collectdRulesTemplate = templates.getResource('collectd_rules')
    }

    def configureSELinux() {
        log.info 'Configure SELinux rules.'
        def tmp = createTmpDir()
        try {
            template resource: collectdRulesTemplate, name: 'collectdRules', vars: [:], dest: "$tmp/collectd_t.te" call()
            shell privileged: true, sudoChdir: tmp, """
make -f /usr/share/selinux/devel/Makefile
semodule -i "collectd_t.pp"
""" call()
        } finally {
            deleteTmpFile privileged: true, file: tmp
        }
    }

    @Override
    ContextProperties getDefaultProperties() {
        propertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }

    @Override
    String getSystemName() {
        SYSTEM_NAME
    }

    @Override
    String getSystemVersion() {
        SYSTEM_VERSION
    }
}
