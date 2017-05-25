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
package com.anrisoftware.sscontrol.etcd.upstream.external

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.etcd.external.Authentication
import com.anrisoftware.sscontrol.etcd.external.Etcd
import com.anrisoftware.sscontrol.etcd.external.Binding.BindingFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.tls.external.Tls

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Etcd</i> 3.1 service from the upstream sources for Systemd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Etcd_3_1_Upstream_Systemd extends ScriptBase {

    TemplateResource servicesTemplate

    TemplateResource configsTemplate

    @Inject
    BindingFactory bindingFactory

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory, NumberTrueRenderer numberTrueRenderer) {
        def attrs = [renderers: [numberTrueRenderer]]
        def templates = templatesFactory.create('Etcd_3_1_Upstream_Systemd_Templates', attrs)
        this.servicesTemplate = templates.getResource('etcd_services')
        this.configsTemplate = templates.getResource('etcd_configs')
    }

    def setupDefaults() {
        Etcd service = service
        if (!service.debugLogging.modules['debug']) {
            service.debug 'debug', level: defaultDebugLogLevel
        }
        if (service.bindings.size() == 0) {
            service.bindings << bindingFactory.create()
        }
        if (!service.bindings[0].address) {
            service.bindings[0].address = defaultBindingAddress
        }
        if (service.advertises.size() == 0) {
            service.advertises << bindingFactory.create()
        }
        if (!service.advertises[0].address) {
            service.advertises[0].address = defaultAdvertiseAddress
        }
        if (!service.memberName) {
            service.memberName = defaultMemberName
        }
        if (!service.tls.caName) {
            service.tls.caName = defaultClientTlsCaName
        }
        if (!service.tls.certName) {
            service.tls.certName = defaultClientTlsCertName
        }
        if (!service.tls.keyName) {
            service.tls.keyName = defaultClientTlsKeyName
        }
        service.authentications.findAll { it.tls  } each {
            Tls tls = it.tls
            if (!tls.caName) {
                tls.caName = defaultClientAuthenticationTlsCaName[it.type]
            }
            if (!tls.certName) {
                tls.certName = defaultClientAuthenticationTlsCertName[it.type]
            }
            if (!tls.keyName) {
                tls.keyName = defaultClientAuthenticationTlsKeyName[it.type]
            }
        }
        if (service.peer) {
            if (!service.peer.tls.caName) {
                service.peer.tls.caName = defaultPeerTlsCaName
            }
            if (!service.peer.tls.certName) {
                service.peer.tls.certName = defaultPeerTlsCertName
            }
            if (!service.peer.tls.keyName) {
                service.peer.tls.keyName = defaultPeerTlsKeyName
            }
            service.peer.authentications.findAll { it.tls  } each {
                Tls tls = it.tls
                if (!tls.caName) {
                    tls.caName = defaultPeerAuthenticationTlsCaName[it.type]
                }
                if (!tls.certName) {
                    tls.certName = defaultPeerAuthenticationTlsCertName[it.type]
                }
                if (!tls.keyName) {
                    tls.keyName = defaultPeerAuthenticationTlsKeyName[it.type]
                }
            }
        }
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
        shell privileged: true, "systemctl daemon-reload" call()
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

    def uploadClientTls() {
        uploadTls 'service.tls', service.tls
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
        log.info 'Uploads etcd $name certificates.'
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

    String getUser() {
        properties.getProperty "user", defaultProperties
    }

    def getDefaultDebugLogLevel() {
        properties.getNumberProperty 'default_debug_log_level', defaultProperties intValue()
    }

    URI getDefaultBindingAddress() {
        properties.getURIProperty 'default_binding_address', defaultProperties
    }

    URI getDefaultAdvertiseAddress() {
        properties.getURIProperty 'default_advertise_address', defaultProperties
    }

    def getDefaultMemberName() {
        properties.getProperty 'default_member_name', defaultProperties
    }

    def getDefaultClientTlsCaName() {
        properties.getProperty 'default_tls_ca_name', defaultProperties
    }

    def getDefaultClientTlsCertName() {
        properties.getProperty 'default_tls_cert_name', defaultProperties
    }

    def getDefaultClientTlsKeyName() {
        properties.getProperty 'default_tls_key_name', defaultProperties
    }

    def getDefaultPeerTlsCaName() {
        properties.getProperty 'default_peer_tls_ca_name', defaultProperties
    }

    def getDefaultPeerTlsCertName() {
        properties.getProperty 'default_peer_tls_cert_name', defaultProperties
    }

    def getDefaultPeerTlsKeyName() {
        properties.getProperty 'default_peer_tls_key_name', defaultProperties
    }

    Map getDefaultClientAuthenticationTlsCaName() {
        def s = properties.getProperty 'default_authentication_tls_ca_name', defaultProperties
        Eval.me s
    }

    Map getDefaultClientAuthenticationTlsCertName() {
        def s = properties.getProperty 'default_authentication_tls_cert_name', defaultProperties
        Eval.me s
    }

    Map getDefaultClientAuthenticationTlsKeyName() {
        def s = properties.getProperty 'default_authentication_tls_key_name', defaultProperties
        Eval.me s
    }

    Map getDefaultPeerAuthenticationTlsCaName() {
        def s = properties.getProperty 'default_peer_authentication_tls_ca_name', defaultProperties
        Eval.me s
    }

    Map getDefaultPeerAuthenticationTlsCertName() {
        def s = properties.getProperty 'default_peer_authentication_tls_cert_name', defaultProperties
        Eval.me s
    }

    Map getDefaultPeerAuthenticationTlsKeyName() {
        def s = properties.getProperty 'default_peer_authentication_tls_key_name', defaultProperties
        Eval.me s
    }

    @Override
    def getLog() {
        log
    }
}
