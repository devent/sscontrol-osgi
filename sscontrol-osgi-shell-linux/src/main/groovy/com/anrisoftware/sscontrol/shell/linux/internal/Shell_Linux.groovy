package com.anrisoftware.sscontrol.shell.linux.internal

import static com.anrisoftware.sscontrol.shell.linux.internal.Shell_Linux_Service.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.shell.external.Shell

import groovy.util.logging.Slf4j

/**
 * Runs scripts on GNU/Linux systems.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Shell_Linux extends ScriptBase {

    @Inject
    Shell_Linux_Properties linuxPropertiesProvider

    @Override
    ContextProperties getDefaultProperties() {
        linuxPropertiesProvider.get()
    }

    @Override
    def run() {
        Shell service = this.service
        service.scripts.each {
            log.trace 'Run script {}', it
            shell it.vars call()
        }
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
