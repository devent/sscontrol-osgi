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
