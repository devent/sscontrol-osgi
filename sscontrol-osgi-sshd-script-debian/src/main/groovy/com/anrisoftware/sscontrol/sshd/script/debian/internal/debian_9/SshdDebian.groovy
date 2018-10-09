package com.anrisoftware.sscontrol.sshd.script.debian.internal.debian_9

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.sshd.script.debian.external.SshdSystemd
import com.anrisoftware.sscontrol.sshd.service.external.Sshd
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_9_UtilsFactory

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Sshd</i> 6 service for Debian 9.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class SshdDebian extends SshdSystemd {

    @Inject
    SshdDebianProperties debianPropertiesProvider

    @Inject
    SshdDebianUfwFactory ufwFactory

    DebianUtils debian

    @Inject
    void setDebianUtilsFactory(Debian_9_UtilsFactory factory) {
        this.debian = factory.create this
    }

    @Override
    def run() {
        setupDefaults()
        installPackages()
        configureService()
        restartService()
        ufwFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
    }

    def setupDefaults() {
        Sshd service = service
        if (!service.debugLogging.modules['debug']) {
            service.debug level: defaultLogLevel
        }
        if (!service.binding.port) {
            service.bind port: defaultPort
        }
    }

    def getDefaultPort() {
        getScriptNumberProperty 'default_port' intValue()
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    Sshd getService() {
        super.getService()
    }

    @Override
    def getLog() {
        log
    }
}
