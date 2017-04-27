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
package com.anrisoftware.sscontrol.repo.git.linux.internal

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * <i>Git</i> code repository service for Linux.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class GitRepo_Linux extends ScriptBase {

    @Inject
    GitRepo_Linux_Properties linuxPropertiesProvider

    @Override
    def run() {
    }

    @Override
    ContextProperties getDefaultProperties() {
        linuxPropertiesProvider.get()
    }

    def runKubectl(Map vars) {
        upstreamLinux.runKubectl vars
    }

    def uploadCertificates(Map vars) {
        upstreamLinux.uploadCertificates(vars)
    }

    @Override
    def getLog() {
        log
    }
}
