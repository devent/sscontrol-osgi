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
package com.anrisoftware.sscontrol.flanneldocker.upstream.external

import static org.apache.commons.io.FilenameUtils.getBaseName
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import javax.inject.Inject

import org.apache.commons.io.FilenameUtils

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.flanneldocker.external.FlannelDocker
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.tls.external.Tls

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Flannel-Docker</i> 0.7 service from the upstream sources.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class FlannelDocker_0_7_Upstream extends ScriptBase {

    TemplateResource installCmdResource

    @Inject
    def setTemplates(TemplatesFactory factory) {
        def t = factory.create 'FlannelDocker_0_7_Upstream_Templates'
        this.installCmdResource = t.getResource 'install_flannel'
    }

    def setupDefaults() {
        log.info 'Setup Flannel-Docker defaults.'
        FlannelDocker service = this.service
        assertThat "etcd.endpoints>0", service.etcd.endpoints.size(), greaterThan(0)
        if (!service.debugLogging.modules['debug']) {
            service.debug 'debug', level: defaultDebugLogLevel
        }
        if (!service.etcd.prefix) {
            service.etcd.prefix = defaultEtcdPrefix
        }
        if (!service.backend) {
            service.backend defaultFlannelBackendType
        }
        if (!service.network.address) {
            service.network.address = defaultFlannelNetworkAddress
        }
        if (service.etcd) {
            Tls tls = service.etcd.tls
            if (!tls.caName) {
                tls.caName = defaultEtcdCaName
            }
            if (!tls.certName) {
                tls.certName = defaultEtcdCertName
            }
            if (!tls.keyName) {
                tls.keyName = defaultEtcdKeyName
            }
        }
    }

    def installFlannel() {
        log.info 'Installs Flannel-Docker.'
        FlannelDocker service = this.service
        copy src: archive, hash: archiveHash, dest: "/tmp", direct: true, timeout: timeoutLong call()
        shell resource: installCmdResource, name: 'installCmd' call()
    }

    URI getArchive() {
        properties.getURIProperty 'flannel_archive', defaultProperties
    }

    String getArchiveFile() {
        FilenameUtils.getName(archive.toString())
    }

    String getArchiveName() {
        getBaseName(getBaseName(archive.toString()))
    }

    String getArchiveHash() {
        properties.getProperty 'flannel_archive_hash', defaultProperties
    }

    File getCertsDir() {
        properties.getFileProperty "certs_dir", base, defaultProperties
    }

    File getLibexecDir() {
        properties.getFileProperty "libexec_dir", base, defaultProperties
    }

    def getDefaultDebugLogLevel() {
        properties.getNumberProperty 'default_debug_log_level', defaultProperties intValue()
    }

    def getDefaultEtcdPrefix() {
        properties.getProperty 'default_etcd_prefix', defaultProperties
    }

    def getDefaultFlannelBackendType() {
        properties.getProperty 'default_flannel_backend_type', defaultProperties
    }

    def getDefaultFlannelNetworkAddress() {
        properties.getProperty 'default_flannel_network_address', defaultProperties
    }

    String getDefaultEtcdCaName() {
        properties.getProperty "default_etcd_ca_name", defaultProperties
    }

    String getDefaultEtcdCertName() {
        properties.getProperty "default_etcd_cert_name", defaultProperties
    }

    String getDefaultEtcdKeyName() {
        properties.getProperty "default_etcd_key_name", defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
