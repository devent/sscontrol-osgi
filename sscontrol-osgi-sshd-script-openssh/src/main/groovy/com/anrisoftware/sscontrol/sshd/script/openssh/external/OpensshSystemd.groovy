package com.anrisoftware.sscontrol.sshd.script.openssh.external

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.sshd.service.external.Sshd

import groovy.util.logging.Slf4j

/**
 * OpenSSH service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class OpensshSystemd extends ScriptBase {

    def configureService() {
        log.info 'Configuring sshd service.'
        Sshd service = service
        int debug = service.debugLogging.modules['debug'].level
        replace "s/(?m)^#?\\s*LogLevel.*/LogLevel ${logLevelMap[debug]}/", privileged: true, dest: configFile call()
        replace "s/(?m)^#?\\s*PermitRootLogin.*/PermitRootLogin ${permitRootLogin}/", privileged: true, dest: configFile call()
        replace "s/(?m)^#?\\s*PasswordAuthentication.*/PasswordAuthentication ${passwordAuthentication}/", privileged: true, dest: configFile call()
        if (service.binding.port) {
            replace "s/(?m)^#?\\s*Port.*/Port ${service.binding.port}/", privileged: true, dest: configFile call()
        }
    }

    Map getLogLevelMap() {
        Eval.me getScriptProperty('log_level_map')
    }

    String getPermitRootLogin() {
        getScriptProperty 'permit_root_login'
    }

    String getPasswordAuthentication() {
        getScriptProperty 'password_authentication'
    }

    @Override
    def getLog() {
        log
    }
}
