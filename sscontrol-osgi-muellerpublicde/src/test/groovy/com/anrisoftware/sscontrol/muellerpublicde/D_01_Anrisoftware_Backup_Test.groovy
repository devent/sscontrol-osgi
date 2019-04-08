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
class D_01_Anrisoftware_Backup_Test extends Abstract_Runner_Debian_Test {

    @Test
    void "backup-postgres-anrisoftware-com"() {
        def test = [
            name: "backup-anrisoftware-com",
            script: '''
service "ssh" with {
    host "robobee@localhost", socket: localhostSocket
}
service "ssh", group: "master" with {
    host "robobee@andrea-master-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "k8s-cluster", target: 'master' with {
    credentials type: 'cert', name: 'robobee-admin', ca: k8s_vars.admin.certs.ca, cert: k8s_vars.admin.certs.cert, key: k8s_vars.admin.certs.key
}

def dailyPath = "/home/${System.getProperty('user.name')}/Private/andrea-master-0.muellerpublic.de/backups/daily"
service "backup" with {
    service namespace: "anrisoftware-com", name: "postgres"
    destination arguments: "-c --delete", dir: "${dailyPath}/anrisoftware-com/postgres"
    client key: k8s_vars.rsync.key, proxy: true, timeout: "1h"
}
''',
            scriptVars: [
                localhostSocket: localhostSocket,
                socketFiles: socketFiles,
                k8s_vars: k8s_vars
            ],
            expectedServicesSize: 3,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @Test
    void "backup-gitea-anrisoftware-com"() {
        def test = [
            name: "backup-anrisoftware-com",
            script: '''
service "ssh" with {
    host "robobee@localhost", socket: localhostSocket
}
service "ssh", group: "master" with {
    host "robobee@andrea-master-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "k8s-cluster", target: 'master' with {
    credentials type: 'cert', name: 'robobee-admin', ca: k8s_vars.admin.certs.ca, cert: k8s_vars.admin.certs.cert, key: k8s_vars.admin.certs.key
}

def dailyPath = "/home/${System.getProperty('user.name')}/Private/andrea-master-0.muellerpublic.de/backups/daily"
service "backup" with {
    service namespace: "anrisoftware-com", name: "gitea"
    destination arguments: "-c --delete", dir: "${dailyPath}/anrisoftware-com/gitea"
    client key: k8s_vars.rsync.key, proxy: true, timeout: "4h"
    source << "/html"
    source << "/data"
}
''',
            scriptVars: [
                localhostSocket: localhostSocket,
                socketFiles: socketFiles,
                k8s_vars: k8s_vars
            ],
            expectedServicesSize: 3,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @Test
    void "backup-redmine-anrisoftware-com"() {
        def test = [
            name: "backup-anrisoftware-com",
            script: '''
service "ssh" with {
    host "robobee@localhost", socket: localhostSocket
}
service "ssh", group: "master" with {
    host "robobee@andrea-master-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "k8s-cluster", target: 'master' with {
    credentials type: 'cert', name: 'robobee-admin', ca: k8s_vars.admin.certs.ca, cert: k8s_vars.admin.certs.cert, key: k8s_vars.admin.certs.key
}

def dailyPath = "/home/${System.getProperty('user.name')}/Private/andrea-master-0.muellerpublic.de/backups/daily"
service "backup" with {
    service namespace: "anrisoftware-com", name: "redmine-app"
    destination arguments: "-c --delete", dir: "${dailyPath}/anrisoftware-com/redmine"
    client key: k8s_vars.rsync.key, proxy: true, timeout: "4h"
}
''',
            scriptVars: [
                localhostSocket: localhostSocket,
                socketFiles: socketFiles,
                k8s_vars: k8s_vars
            ],
            expectedServicesSize: 3,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @Before
    void checkProfile() {
        assumeMastersExists()
        assumeNodesExists()
        assumeLocalExists()
    }
}
