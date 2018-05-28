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
package com.anrisoftware.sscontrol.k8s.fromhelm.script.linux.internal.script_1_9

import static org.apache.commons.io.FilenameUtils.getBaseName
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import org.apache.commons.io.FilenameUtils

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8s.fromhelm.service.external.FromHelm

import groovy.util.logging.Slf4j

/**
 * From Helm service for Kubernetes 1.9.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class AbstractFromHelmLinux extends ScriptBase {

    @Override
    def run() {
	FromHelm service = service
	assertThat "clusters=0 for $service", service.clusterHosts.size(), greaterThan(0)
    }

    /**
     * Installs Helm on the cluster hosts.
     */
    def installHelm() {
	log.info 'Installs Helm.'
	copy src: archive, hash: archiveHash, dest: "/tmp", direct: true, timeout: timeoutLong call()
	def archiveFile = FilenameUtils.getName(archive.toString())
	def archiveName = getBaseName(getBaseName(archive.toString()))
	shell timeout: timeoutMiddle, """\
cd /tmp
tar xf "$archiveFile"
cd "$archiveName"
sudo find . -executable -type f -exec cp '{}' '$binDir' \\;
sudo chmod o+rx '$binDir'/*
""" call()
    }

    /**
     * Installs a chart from a specified source repository.
     */
    def fromRepo() {
	File dir = getState "${service.repo.type}-${service.repo.repo.group}-dir"
	assertThat "checkout-dir=null for $service", dir, notNullValue()
    }

    /**
     * Installs a chart.
     */
    def fromChart() {
    }

    @Override
    def getLog() {
	log
    }
}
