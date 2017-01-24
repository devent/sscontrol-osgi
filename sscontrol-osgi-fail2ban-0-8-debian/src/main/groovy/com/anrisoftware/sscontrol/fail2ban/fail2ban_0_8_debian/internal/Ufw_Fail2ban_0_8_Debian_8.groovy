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
package com.anrisoftware.sscontrol.fail2ban.fail2ban_0_8_debian.internal

import static com.anrisoftware.sscontrol.fail2ban.fail2ban_0_8_debian.internal.Fail2ban_0_8_Debian_8_Service.*

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.fail2ban.fail2ban_0_8_debian.external.Ufw_Fail2ban_0_8_Debian

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class Ufw_Fail2ban_0_8_Debian_8 extends Ufw_Fail2ban_0_8_Debian {

    @Inject
    Ufw_Fail2ban_0_8_Debian_8_Properties debianPropertiesProvider

    @Inject
    TemplatesFactory templatesFactory

    @Override
    def run() {
        installPackages()
        configureService()
        configureActions()
        enableService()
    }

    def configureActions() {
        def t = templatesFactory.create 'Ufw_Fail2ban_0_8_Debian_8_Templates'
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

    @Override
    String getSystemName() {
        SYSTEM_NAME
    }

    @Override
    String getSystemVersion() {
        SYSTEM_VERSION
    }
}
