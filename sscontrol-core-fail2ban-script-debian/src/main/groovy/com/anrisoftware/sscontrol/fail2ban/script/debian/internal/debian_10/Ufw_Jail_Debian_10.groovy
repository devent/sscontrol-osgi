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
package com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian_10

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.Templates
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.resources.texts.external.Texts
import com.anrisoftware.resources.texts.external.TextsFactory
import com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian.Ufw_Jail_0_10_Debian
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_10_UtilsFactory

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class Ufw_Jail_Debian_10 extends Ufw_Jail_0_10_Debian {

    @Inject
    Ufw_Jail_Debian_10_Properties debianPropertiesProvider

    DebianUtils debian

    Templates debianTemplates

    Texts debianTexts

    @Inject
    void setTemplatesFactory(TemplatesFactory factory) {
        debianTemplates = factory.create 'Ufw_Fail2ban_Debian_10_Templates'
    }

    @Inject
    void setTextsFactory(TextsFactory factory) {
        debianTexts = factory.create 'Ufw_Fail2ban_Debian_10_Texts'
    }

    @Inject
    void setDebian(Debian_10_UtilsFactory factory) {
        this.debian = factory.create this
    }

    @Override
    def run() {
        installPackages()
        configureActions()
        configureFilters()
        enableService()
        configureJail()
    }

    def configureActions() {
        template resource: debianTemplates.getResource('ufw_action'), name: "ufwAction", privileged: true, dest: "$actionsDir/ufw.conf" call()
    }

    def configureFilters() {
        copyString privileged: true, str: debianTexts.getResource('sshd_filter').text, dest: "$filtersDir/sshd.conf"
    }

    /**
     * Returns the directory for jail actions, usually in {@code action.d}
     * <ul>
     * <li>Property {@code actions_dir}
     * </ul>
     */
    File getActionsDir() {
        properties.getFileProperty "actions_dir", configDir, defaultProperties
    }

    /**
     * Returns the directory for jail filters, usually in {@code filter.d}
     * <ul>
     * <li>Property {@code filters_dir}
     * </ul>
     */
    File getFiltersDir() {
        properties.getFileProperty "filters_dir", configDir, defaultProperties
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
