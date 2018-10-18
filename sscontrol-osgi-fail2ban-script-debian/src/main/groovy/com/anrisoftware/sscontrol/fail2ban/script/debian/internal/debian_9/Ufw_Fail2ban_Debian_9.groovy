/*-
 * #%L
 * sscontrol-osgi - fail2ban-script-debian
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
package com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian_9

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian.Ufw_Fail2ban_0_9_Debian
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_9_UtilsFactory

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class Ufw_Fail2ban_Debian_9 extends Ufw_Fail2ban_0_9_Debian {

    @Inject
    Ufw_Fail2ban_Debian_9_Properties debianPropertiesProvider

    @Inject
    TemplatesFactory templatesFactory

    DebianUtils debian

    @Inject
    void setDebian(Debian_9_UtilsFactory factory) {
        this.debian = factory.create this
    }

    @Override
    def run() {
        installPackages()
        configureService()
        configureActions()
        enableService()
    }

    def configureActions() {
        def t = templatesFactory.create 'Ufw_Fail2ban_Debian_9_Templates'
        template resource: t.getResource('ufw_action'), name: "ufwAction", privileged: true, dest: "$actionsDir/ufw.conf" call()
    }

    File getActionsDir() {
        properties.getFileProperty "actions_dir", defaultProperties
    }

    @Override
    Properties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
