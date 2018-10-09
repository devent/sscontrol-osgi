package com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_9.etcd_3_2

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.etcd.script.upstream.external.Etcd_3_x_Upstream_Systemd
import com.anrisoftware.sscontrol.etcd.service.external.Etcd

import groovy.util.logging.Slf4j

/**
 * Configures the Etcd 3.2 service from the upstream sources
 * for Systemd and Debian 9.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class EtcdUpstreamSystemdDebian extends Etcd_3_x_Upstream_Systemd {

    @Inject
    EtcdDebianProperties debianPropertiesProvider

    @Override
    Object run() {
        Etcd service = this.service
        createDirectories()
        def services = { createServices() }
        def config = { createConfig() }
        if (service.proxy) {
            services = { createProxyServices() }
            config = { createProxyConfig() }
        }
        if (service.gateway) {
            services = { createGatewayServices() }
            config = { createGatewayConfig() }
        }
        services()
        config()
        uploadServerTls()
        uploadClientTls()
        uploadClientCertAuth()
        uploadPeerTls()
        uploadPeerCertAuth()
        secureSslDir()
        createEctdctlVariablesFile()
        reloadDaemon()
        enableServices()
        startServices()
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
