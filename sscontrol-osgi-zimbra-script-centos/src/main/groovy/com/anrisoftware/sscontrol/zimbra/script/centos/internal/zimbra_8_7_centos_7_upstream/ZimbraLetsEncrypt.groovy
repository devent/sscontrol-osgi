package com.anrisoftware.sscontrol.zimbra.script.centos.internal.zimbra_8_7_centos_7_upstream

import static org.apache.commons.io.FilenameUtils.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.zimbra.script.centos.internal.zimbra_8_7_centos_7_letsencrypt_docker_systemd.ZimbraLetsEncryptDockerSystemd

import groovy.util.logging.Slf4j

/**
 * Uses docker to install certbot to create LetsEncrypt certificates for Zimbra.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class ZimbraLetsEncrypt extends ZimbraLetsEncryptDockerSystemd {

    @Inject
    Zimbra_Properties propertiesProvider

    @Override
    ContextProperties getDefaultProperties() {
        propertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
