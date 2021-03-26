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
package com.anrisoftware.sscontrol.etcd.script.upstream.external

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.etcd.service.external.Authentication
import com.anrisoftware.sscontrol.etcd.service.external.Etcd
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.tls.external.Tls
import com.anrisoftware.sscontrol.utils.st.miscrenderers.external.NumberTrueRenderer
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtilsFactory

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Etcd</i> 3.x service from the upstream sources for Systemd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Etcd_3_x_Upstream_Systemd extends ScriptBase {

    TemplateResource servicesTemplate

    TemplateResource configsTemplate

    TemplateResource etcdctlVariablesTemplate

    TemplateResource proxyServicesTemplate

    TemplateResource proxyConfigsTemplate

    TemplateResource gatewayServicesTemplate

    TemplateResource gatewayConfigsTemplate

    SystemdUtils systemd

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory, NumberTrueRenderer numberTrueRenderer) {
        def attrs = [renderers: [numberTrueRenderer]]
        def templates = templatesFactory.create('Etcd_3_x_Upstream_Systemd_Templates', attrs)
        this.servicesTemplate = templates.getResource('etcd_services')
        this.configsTemplate = templates.getResource('etcd_configs')
        this.etcdctlVariablesTemplate = templates.getResource('etcdctl_variables')
        templates = templatesFactory.create('Etcd_Proxy_3_x_Upstream_Systemd_Templates')
        this.proxyServicesTemplate = templates.getResource('etcd_proxy_services')
        this.proxyConfigsTemplate = templates.getResource('etcd_proxy_configs')
        templates = templatesFactory.create('Etcd_Gateway_3_x_Upstream_Systemd_Templates')
        this.gatewayServicesTemplate = templates.getResource('etcd_gateway_services')
        this.gatewayConfigsTemplate = templates.getResource('etcd_gateway_configs')
    }

    @Inject
    void setSystemd(SystemdUtilsFactory factory) {
        this.systemd = factory.create this
    }

    def createDirectories() {
        log.info 'Create etcd directories.'
        def dir = systemdSystemDir
        def datadir = dataDir
        def certsdir = certsDir
        shell privileged: true, """
mkdir -p '$dir'
mkdir -p '$datadir'
useradd -r $user
chown $user '$datadir'
mkdir -p '$certsdir'
chown ${user}.root '$certsdir'
chmod o-rx '$certsdir'
""" call()
    }

    def createServices() {
        log.info 'Create etcd services.'
        def dir = systemdSystemDir
        [
            [
                resource: servicesTemplate,
                name: 'etcdService',
                privileged: true,
                dest: "$dir/etcd.service",
                vars: [:],
            ],
        ].each { template it call() }
    }

    def createConfig() {
        log.info 'Create etcd configuration.'
        def dir = configDir
        shell privileged: true, "mkdir -p $dir" call()
        [
            [
                resource: configsTemplate,
                name: 'etcdConfig',
                privileged: true,
                dest: "$configDir/etcd.conf",
                vars: [:],
            ],
        ].each { template it call() }
    }

    def uploadServerTls() {
        uploadTls 'service.tls', service.tls
    }

    def uploadClientTls() {
        if (service.client) {
            uploadTls 'service.client.tls', service.client.tls
        }
    }

    def uploadPeerTls() {
        if (service.peer) {
            uploadTls 'service.peer.tls', service.peer.tls
        }
    }

    def uploadTls(String name, Tls tls) {
        log.info 'Uploads etcd {} certificates.', name
        def certsdir = certsDir
        Etcd service = service
        [
            [
                name: "${name}.ca",
                src: tls.ca,
                dest: "$certsdir/$tls.caName",
                privileged: true,
            ],
            [
                name: "${name}.cert",
                src: tls.cert,
                dest: "$certsdir/$tls.certName",
                privileged: true,
            ],
            [
                name: "${name}.key",
                src: tls.key,
                dest: "$certsdir/$tls.keyName",
                privileged: true,
            ],
        ].each {
            if (it.src) {
                copyResource it
            }
        }
    }

    def uploadClientCertAuth() {
        Etcd service = service
        uploadCertAuth 'service.authentications', service.authentications
    }

    def uploadPeerCertAuth() {
        Etcd service = service
        if (service.peer) {
            uploadCertAuth 'service.peer.authentications', service.peer.authentications
        }
    }

    def uploadCertAuth(String name, List<Authentication> auth) {
        log.info 'Uploads etcd {} certificates.', name
        def certsdir = certsDir
        auth.findAll { it.tls  } each {
            Tls tls = it.tls
            [
                [
                    name: "${name}.ca",
                    src: tls.ca,
                    dest: "$certsdir/$tls.caName",
                    privileged: true
                ],
                [
                    name: "${name}.cert",
                    src: tls.cert,
                    dest: "$certsdir/$tls.certName",
                    privileged: true
                ],
                [
                    name: "${name}.key",
                    src: tls.key,
                    dest: "$certsdir/$tls.keyName",
                    privileged: true
                ],
            ].each {
                if (it.src) {
                    copyResource it
                }
            }
        }
    }

    def createEctdctlVariablesFile() {
        def file = etcdctlVariablesFile
        log.info 'Create etcdctl variables file', file
        def a = [
            resource: etcdctlVariablesTemplate,
            name: 'etcdctlVariables',
            privileged: true,
            dest: file,
            vars: [:],
        ]
        template a call()
    }

    def secureSslDir() {
        def dir = certsDir
        log.info 'Secure ssl directory {}', dir
        shell privileged: true, """
chown -R "${user}:root" "$dir"
chmod -R o-rwX $dir
""" call()
    }

    def createProxyServices() {
        log.info 'Create etcd-proxy services.'
        def dir = systemdSystemDir
        [
            [
                resource: proxyServicesTemplate,
                name: 'etcdProxyService',
                privileged: true,
                dest: "$dir/etcd-proxy.service",
                vars: [:],
            ],
        ].each { template it call() }
    }

    def createProxyConfig() {
        log.info 'Create etcd-proxy configuration.'
        def dir = configDir
        shell privileged: true, "mkdir -p $dir" call()
        [
            [
                resource: proxyConfigsTemplate,
                name: 'etcdProxyConfig',
                privileged: true,
                dest: "$configDir/etcd-proxy.conf",
                vars: [:],
            ],
        ].each { template it call() }
    }

    def createGatewayServices() {
        log.info 'Create etcd-gateway services.'
        def dir = systemdSystemDir
        [
            [
                resource: gatewayServicesTemplate,
                name: 'etcdGatewayService',
                privileged: true,
                dest: "$dir/etcd-gateway.service",
                vars: [:],
            ],
        ].each { template it call() }
    }

    def createGatewayConfig() {
        log.info 'Create etcd-gateway configuration.'
        def dir = configDir
        shell privileged: true, "mkdir -p $dir" call()
        [
            [
                resource: gatewayConfigsTemplate,
                name: 'etcdGatewayConfig',
                privileged: true,
                dest: "$configDir/etcd-gateway.conf",
                vars: [:],
            ],
        ].each { template it call() }
    }

    def reloadDaemon() {
        systemd.reloadDaemon()
    }

    def startServices() {
        Etcd service = service
        if (service.proxy) {
            def services = this.proxyServices
            systemd.startServices services: services, timeout: timeoutShort
            return
        }
        if (service.gateway) {
            def services = this.gatewayServices
            systemd.startServices services: services, timeout: timeoutShort
            return
        }
        def services = this.services
        systemd.startServices services: services, timeout: timeoutMiddle, delayed: true
    }

    def restartServices() {
        Etcd service = service
        def services = this.services
        if (service.proxy) {
            services = this.proxyServices
        }
        if (service.gateway) {
            services = this.gatewayServices
        }
        systemd.restartServices services
    }

    def enableServices() {
        Etcd service = service
        def services = this.services
        if (service.proxy) {
            services = this.proxyServices
        }
        if (service.gateway) {
            services = this.gatewayServices
        }
        systemd.enableServices services
    }

    def stopServices() {
        Etcd service = service
        def services = this.services
        if (service.proxy) {
            services = this.proxyServices
        }
        if (service.gateway) {
            services = this.gatewayServices
        }
        systemd.stopServices services
    }

    File getSystemdSystemDir() {
        properties.getFileProperty "systemd_system_dir", base, defaultProperties
    }

    File getBinDir() {
        properties.getFileProperty "bin_dir", base, defaultProperties
    }

    File getDataDir() {
        properties.getFileProperty "data_dir", base, defaultProperties
    }

    File getCertsDir() {
        properties.getFileProperty "certs_dir", base, defaultProperties
    }

    File getEtcdctlVariablesFile() {
        properties.getFileProperty "etcdctl_variables_file", base, defaultProperties
    }

    String getUser() {
        properties.getProperty "user", defaultProperties
    }

    /**
     * Returns the services of the proxy service.
     *
     * <ul>
     * <li>profile property {@code proxy_services}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getProxyServices() {
        properties.getListProperty "proxy_services", defaultProperties
    }

    File getProxyConfigFile() {
        getFileProperty "proxy_config_file", configDir, defaultProperties
    }

    /**
     * Returns the services of the gateway service.
     *
     * <ul>
     * <li>profile property {@code gateway_services}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getGatewayServices() {
        properties.getListProperty "gateway_services", defaultProperties
    }

    File getGatewayConfigFile() {
        getFileProperty "gateway_config_file", configDir, defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
