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
package com.anrisoftware.sscontrol.fail2ban.script.centos.internal.centos_7

import javax.inject.Inject

import com.anrisoftware.sscontrol.fail2ban.script.centos.internal.centos.Fail2ban_Centos
import com.anrisoftware.sscontrol.fail2ban.script.fail2ban_0_x.external.Jail_0_x
import com.anrisoftware.sscontrol.utils.centos.external.CentosUtils
import com.anrisoftware.sscontrol.utils.centos.external.Centos_7_UtilsFactory

/**
 * Configures the <i>Fail2ban</i> service for CentOS 7.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
class Fail2ban_Centos_7 extends Fail2ban_Centos {

    @Inject
    Fail2ban_Centos_7_Properties centosPropertiesProvider

    @Inject
    IptablesMultiportJail_Centos_7_Factory iptablesMultiportJail

    @Inject
    FirewalldJail_Centos_7_Factory firewalldJail

    CentosUtils centos

    @Inject
    void setCentos(Centos_7_UtilsFactory factory) {
        this.centos = factory.create this
    }

    @Override
    def run() {
        setupDefaults()
        stopService()
        installPackages()
        configureService()
        jailScript.run()
        enableService()
        startService()
    }

    /**
     * Provides the jail script for {@code iptables-multiport}.
     */
    Jail_0_x getIptablesMultiportJailScript() {
        iptablesMultiportJail.create scriptsRepository, service, target, threads, scriptEnv
    }

    /**
     * Provides the jail script for {@code firewalld}.
     */
    Jail_0_x getFirewalldJailScript() {
        firewalldJail.create scriptsRepository, service, target, threads, scriptEnv
    }

    @Override
    Properties getDefaultProperties() {
        centosPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
