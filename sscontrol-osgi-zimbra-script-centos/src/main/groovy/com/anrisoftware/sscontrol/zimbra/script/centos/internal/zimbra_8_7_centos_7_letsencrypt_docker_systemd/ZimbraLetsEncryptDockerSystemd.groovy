package com.anrisoftware.sscontrol.zimbra.script.centos.internal.zimbra_8_7_centos_7_letsencrypt_docker_systemd

import static org.apache.commons.io.FilenameUtils.*

import javax.inject.Inject

import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtilsFactory
import com.anrisoftware.sscontrol.zimbra.script.centos.internal.zimbra_8_7_centos_7_letsencrypt_docker_script.ZimbraLetsEncryptDockerScript

import groovy.util.logging.Slf4j

/**
 * Uses docker to install certbot to create LetsEncrypt certificates for Zimbra.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class ZimbraLetsEncryptDockerSystemd extends ZimbraLetsEncryptDockerScript {

    SystemdUtils systemd

    @Inject
    void setSystemdFactory(SystemdUtilsFactory factory) {
        this.systemd = factory.create(this)
    }

    @Override
    def startServiceDocker() {
        systemd.startService 'docker'
    }

    @Override
    def stopDockerService() {
        systemd.stopService 'docker'
    }

    @Override
    def getLog() {
        log
    }
}
