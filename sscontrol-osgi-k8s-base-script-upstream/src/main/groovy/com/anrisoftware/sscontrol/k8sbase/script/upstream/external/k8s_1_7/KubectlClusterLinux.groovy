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
package com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_7

import com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_7.AbstractKubectlLinux

import groovy.util.logging.Slf4j

/**
 * Configures the K8s-Cluster service from the upstream sources for
 * GNU/Linux.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class KubectlClusterLinux extends AbstractKubectlLinux {

    @Override
    Object run() {
    }

    @Override
    def getLog() {
        log
    }
}
