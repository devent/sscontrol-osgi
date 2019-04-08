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
package com.anrisoftware.sscontrol.muellerpublicde

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.muellerpublicde.MuellerpublicdeResources.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import org.junit.Before
import org.junit.Test

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class MuellerpublicTest extends Abstract_Runner_Debian_Test {

    @Test
    void "andrea-mueller use host"() {
        def test = [
            name: "andrea-mueller use host",
            script: '''
service "ssh" with {
    host "robobee@andrea-master-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "ssh", group: "masters" with {
    host "robobee@andrea-master-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "ssh", group: "nodes" with {
    host "robobee@andrea-node-0.muellerpublic.de", socket: socketFiles.nodes[0]
    host "robobee@andrea-node-1.muellerpublic.de", socket: socketFiles.nodes[1]
}
service "k8s-cluster", target: 'masters' with {
    credentials type: 'cert', name: 'robobee-admin', ca: k8s_vars.admin.certs.ca, cert: k8s_vars.admin.certs.cert, key: k8s_vars.admin.certs.key
}
service "repo-git", group: "muellerpublic-de-phpmyadmin" with {
    remote url: "git@github.com:robobee-repos/muellerpublic-de-phpmyadmin.git"
    credentials "ssh", key: robobeeKey
}
service "registry-docker", group: "erwin82" with {
    credentials "user", name: "erwin82", password: "blaue sonne"
}
service "from-repository", repo: "muellerpublic-de-phpmyadmin" with {
}
''',
            scriptVars: [socketFiles: socketFiles, k8s_vars: k8s_vars, robobeeKey: robobeeKey],
            expectedServicesSize: 5,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @Before
    void checkProfile() {
        assumeMastersExists()
        assumeNodesExists()
        assumeTrue "Check hosts: $mastersHosts", isHostAvailable(mastersHosts)
        assumeTrue "Check hosts: $nodesHosts", isHostAvailable(nodesHosts)
    }
}
