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
package com.anrisoftware.sscontrol.flanneldocker.script.upstream.external

import javax.inject.Inject

import com.anrisoftware.sscontrol.flanneldocker.service.external.FlannelDocker
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.ssh.external.SshHost
import com.google.inject.assistedinject.Assisted

/**
 * Returns a list of the host of the nodes.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class NodesTargetsList {

    FlannelDocker service

    HostServices repo

    Object parent

    @Inject
    NodesTargetsList(@Assisted FlannelDocker service,
    @Assisted HostServices repo,
    @Assisted Object parent) {
        super()
        this.service = service
        this.repo = repo
        this.parent = parent
    }

    List<SshHost> getNodes() {
        service.nodes.inject([]) { result, e ->
            result.addAll getAddress(e)
            result
        }
    }

    List<SshHost> getAddress(Object n) {
        def list = []
        if (n instanceof String) {
            if (isValidTargetName(n)) {
                list.addAll nodeTargets(n)
            }
        }
        if (n instanceof SshHost) {
            list << n
        }
        if (n instanceof List) {
            list.addAll list
        }
        list
    }

    def nodeAddressString(String endpoint) {
        new URI(endpoint).host
    }

    def nodeTargets(String target) {
        repo.targets(target).inject([]) { result, e -> result << e }
    }

    boolean isValidTargetName(String n) {
        !new URI(n).scheme
    }
}
