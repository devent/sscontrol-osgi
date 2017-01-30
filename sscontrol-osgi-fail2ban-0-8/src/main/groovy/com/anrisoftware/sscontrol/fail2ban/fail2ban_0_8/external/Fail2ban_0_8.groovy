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
package com.anrisoftware.sscontrol.fail2ban.fail2ban_0_8.external

import org.joda.time.Duration

import com.anrisoftware.sscontrol.fail2ban.external.Backend
import com.anrisoftware.sscontrol.fail2ban.external.Fail2ban
import com.anrisoftware.sscontrol.fail2ban.external.Type
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.types.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Fail2ban 0.8</i> service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Fail2ban_0_8 extends ScriptBase {

    def restartService() {
        log.info 'Restarting fail2ban service.'
        shell privileged: true, "service fail2ban restart" call()
        shell privileged: true, "service fail2ban status" call()
    }

    def setupDefaults() {
        Fail2ban service = service
        if (!service.debugLogging.modules['debug']) {
            service.debug "debug", level: debugLogLevel, target: debugLogTarget
        }
        if (!service.debugLogging.modules['debug'].target) {
            def level = service.debugLogging.modules['debug'].level
            service.debug "debug", level: level, target: debugLogTarget
        }
        if (!service.defaultJail.notify) {
            service.defaultJail.notify address: notifyAddress
        }
        if (!service.defaultJail.ignoreAddresses.size() == 0) {
            service.defaultJail.ignore address: ignoreAddresses
        }
        if (!service.defaultJail.banning.retries) {
            service.defaultJail.banning retries: banningRetries
        }
        if (!service.defaultJail.banning.time) {
            service.defaultJail.banning time: banningTime
        }
        if (!service.defaultJail.banning.backend) {
            service.defaultJail.banning backend: banningBackend
        }
        if (!service.defaultJail.banning.type) {
            service.defaultJail.banning type: banningType
        }
    }

    def configureService() {
        log.info 'Configuring fail2ban service.'
        copyConfFile()
        configureDebugLog()
    }

    def copyConfFile() {
        shell privileged: true, """
cd '${configDir}'
if [ ! -f ${fail2banLocalConfigFileName} ]; then
cp '${fail2banConfigFileName}' '${fail2banLocalConfigFileName}'
fi
if [ ! -f ${jailLocalConfigFileName} ]; then
cp '${jailConfigFileName}' '${jailLocalConfigFileName}'
fi
""" call()
    }

    def configureDebugLog() {
        Fail2ban service = service
        int debugLevel = service.debugLogging.modules['debug'].level
        String debugTarget = service.debugLogging.modules['debug'].target
        replace privileged: true, dest: fail2banLocalConfigFile with {
            line "s/(?m)^loglevel\\s*=.*/loglevel = ${debugLevel}/"
            line "s|(?m)^logtarget\\s*=.*|logtarget = ${debugTarget}|"
            it
        }()
    }

    HostServiceScript getFirewallScript() {
        Fail2ban service = service
        switch (firewall) {
            case 'ufw':
                return ufwScript
                break
        }
    }

    abstract Ufw_Fail2ban_0_8 getUfwScript()

    Integer getDebugLogLevel() {
        properties.getNumberProperty('debug_log_level', defaultProperties)
    }

    String getDebugLogTarget() {
        properties.getProperty('debug_log_target', defaultProperties)
    }

    String getNotifyAddress() {
        properties.getProperty('notify_address', defaultProperties)
    }

    List getIgnoreAddresses() {
        properties.getListProperty('ignore_addresses', defaultProperties)
    }

    Integer getBanningRetries() {
        properties.getNumberProperty('banning_retries', defaultProperties)
    }

    Duration getBanningTime() {
        properties.getDurationProperty('banning_time', defaultProperties)
    }

    Backend getBanningBackend() {
        Backend.valueOf properties.getProperty('banning_backend', defaultProperties)
    }

    Type getBanningType() {
        Type.valueOf properties.getProperty('banning_type', defaultProperties)
    }

    String getFail2banConfigFileName() {
        properties.getProperty "config_file", defaultProperties
    }

    String getJailConfigFileName() {
        properties.getProperty "jail_config_file", defaultProperties
    }

    String getJailLocalConfigFileName() {
        properties.getProperty "jail_local_config_file", defaultProperties
    }

    String getFail2banLocalConfigFileName() {
        properties.getProperty "local_config_file", defaultProperties
    }

    File getFail2banLocalConfigFile() {
        properties.getFileProperty "local_config_file", base, defaultProperties
    }

    String getFirewall() {
        properties.getProperty "firewall", defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
