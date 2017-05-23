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
package com.anrisoftware.sscontrol.icinga.icinga2.script.external

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.icinga.service.external.Feature
import com.anrisoftware.sscontrol.icinga.service.external.Icinga

import groovy.util.logging.Slf4j

/**
 * Configures Icinga 2 service using Systemd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Icinga_2 extends ScriptBase {

    @Inject
    void loadTemplates(TemplatesFactory factory) {
    }

    def setupDefaults() {
        Icinga service = service
    }

    def configureFeatures() {
        log.info "Configure features for {}.", service
        Icinga service = service
        service.features.each { Feature feature ->
            log.debug "Configure feature {}.", feature
            def file = getFeatureFile feature
            def tmp = File.createTempFile("robobee", ".feature")
            try {
                FileUtils.write tmp, feature.script, charset
                copy privileged: true, src: tmp, dest: file call()
            } finally {
                if (tmp) {
                    tmp.delete()
                }
            }
        }
    }

    File getFeaturesAvailableDir() {
        getFileProperty "features_available_dir", base, defaultProperties
    }

    File getFeatureFile(Feature feature) {
        def name = feature.name
        name = name.replaceAll(/-/, "_")
        getFileProperty "feature_${name}_file", featuresAvailableDir, defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}