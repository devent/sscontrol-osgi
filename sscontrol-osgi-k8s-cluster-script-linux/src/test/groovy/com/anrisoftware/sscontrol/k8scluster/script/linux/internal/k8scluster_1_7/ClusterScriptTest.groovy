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
package com.anrisoftware.sscontrol.k8scluster.script.linux.internal.k8scluster_1_7

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
    void "unsecured"() {
        def test = [
            name: "unsecured",
            input: """
service "ssh", host: "localhost"
service "k8s-cluster", target: 'default' with {
    cluster name: 'default-cluster'
    context name: 'default-system'
}
""",
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource ClusterScriptTest, dir, "chmod.out", "${args.test.name}_chmod_expected.txt"
                assertFileResource ClusterScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource ClusterScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource ClusterScriptTest, dir, "wget.out", "${args.test.name}_wget_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "client_cert"() {
        def test = [
            name: "client_cert",
            input: """
service "ssh", host: "localhost"
service "k8s-cluster", target: 'default' with {
    cluster name: 'default-cluster'
    context name: 'default-system'
    credentials type: 'cert', name: 'default-admin', ca: '$certCaPem', cert: '$certCertPem', key: '$certKeyPem'
}
""",
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource ClusterScriptTest, dir, "chmod.out", "${args.test.name}_chmod_expected.txt"
                assertFileResource ClusterScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource ClusterScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource ClusterScriptTest, dir, "wget.out", "${args.test.name}_wget_expected.txt"
                assertFileResource ClusterScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource ClusterScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
            },
        ]
        doTest test
    }
}
