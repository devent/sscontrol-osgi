package com.anrisoftware.sscontrol.ssh.script.linux.internal

import static com.anrisoftware.sscontrol.ssh.script.linux.internal.Ssh_Linux_Service.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.types.ssh.external.Ssh

import groovy.util.logging.Slf4j

/**
 * Configures the <i>ssh</i> on GNU/Linux systems.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class SshLinux extends ScriptBase {

    @Inject
    SshLinuxProperties sshProperties

    @Override
    ContextProperties getDefaultProperties() {
        sshProperties.get()
    }

    @Override
    def run() {
        Ssh ssh = service
        def facts = facts()()
        target.system.system = facts.system.system
        target.system.name = facts.system.name
        target.system.version = facts.system.version
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
