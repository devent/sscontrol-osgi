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
package com.anrisoftware.sscontrol.k8scluster.script.linux.internal.k8scluster_1_8

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import org.junit.Test

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class ClusterScriptTest extends AbstractClusterScriptTest {

    @Test
    void "script with implicit default context"() {
        def test = [
            name: "implicit_context",
            test: "https://project.anrisoftware.com/issues/4020",
            input: '''
service "ssh", host: "localhost", socket: localhostSocket
service "k8s-cluster", target: 'default'
''',
            scriptVars: [localhostSocket: localhostSocket],
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assert new File(dir, "chmod.out").isFile() == false
                assert new File(dir, "mkdir.out").isFile() == false
                assert new File(dir, "sudo.out").isFile() == false
            },
        ]
        doTest test
    }

    @Test
    void "script with unsecured context"() {
        def test = [
            name: "unsecured_context",
            test: "https://project.anrisoftware.com/issues/4019",
            input: '''
service "ssh", host: "localhost", socket: localhostSocket
service "k8s-cluster", target: 'default' with {
    context name: 'default-system'
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assert new File(dir, "chmod.out").isFile() == false
                assert new File(dir, "mkdir.out").isFile() == false
                assert new File(dir, "sudo.out").isFile() == false
            },
        ]
        doTest test
    }

    @Test
    void "script with unsecured cluster and context"() {
        def test = [
            name: "unsecured_cluster",
            input: '''
service "ssh", host: "localhost", socket: localhostSocket
service "k8s-cluster", target: 'default' with {
    cluster name: 'default-cluster'
    context name: 'default-system'
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assert new File(dir, "chmod.out").isFile() == false
                assert new File(dir, "mkdir.out").isFile() == false
                assert new File(dir, "sudo.out").isFile() == false
            },
        ]
        doTest test
    }
}
