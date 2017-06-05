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
package com.anrisoftware.sscontrol.k8s.glusterfsheketi.internal.script_1_6

import static org.hamcrest.Matchers.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.external.Templates
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.external.GlusterfsHeketi

import groovy.util.logging.Slf4j

/**
 * glusterfs-heketi for Kubernetes 1.6.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class GlusterfsHeketi_1_6 extends ScriptBase {

    @Inject
    GlusterfsHeketi_1_6_Properties debianPropertiesProvider

    Templates templates

    @Override
    def run() {
        setupDefaults()
        GlusterfsHeketi service = service
        def args = [:]
        args.targets = service.targets
        args.clusters = service.clusters
        args.repos = service.repos
        def fromRepositoryService = findAvailableService 'from-repository' create args
        fromRepositoryService.vars << service.vars
        fromRepositoryService.serviceProperties = service.serviceProperties
        def fromRepository = createScript 'from-repository', fromRepositoryService
        fromRepository.run()
    }

    def setupDefaults() {
        GlusterfsHeketi service = service
        def vars = service.vars
        if (!vars.heketi) {
            vars.heketi = [:]
            if (!vars.heketi.image) {
                vars.heketi.image = [:]
                if (!vars.heketi.image.name) {
                    vars.heketi.image.name = heketiImageName
                }
                if (!vars.heketi.image.version) {
                    vars.heketi.image.version = heketiImageVersion
                }
            }
            if (!vars.heketi.snapshot) {
                vars.heketi.snapshot = [:]
                if (!vars.heketi.snapshot.limit) {
                    vars.heketi.snapshot.limit = heketiSnapshotLimit
                }
            }
            if (!vars.gluster) {
                vars.gluster = [:]
                vars.gluster.image = [:]
                if (!vars.gluster.image.name) {
                    vars.gluster.image.name = glusterImageName
                }
                if (!vars.gluster.image.version) {
                    vars.gluster.image.version = glusterImageVersion
                }
            }
        }
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    String getHeketiImageName() {
        properties.getProperty 'default_heketi_image_name', defaultProperties
    }

    String getHeketiImageVersion() {
        properties.getProperty 'default_heketi_image_version', defaultProperties
    }

    int getHeketiSnapshotLimit() {
        properties.getNumberProperty 'default_heketi_snapshot_limit', defaultProperties
    }

    String getGlusterImageName() {
        properties.getProperty 'default_gluster_image_name', defaultProperties
    }

    String getGlusterImageVersion() {
        properties.getProperty 'default_gluster_image_version', defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
