package com.anrisoftware.sscontrol.flanneldocker.script.debian.internal.flanneldocker_0_9.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class FlannelDockerScriptTest extends AbstractFlannelDockerScriptTest {

    @Test
    void "script_basic"() {
        def test = [
            name: "script_basic",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "flannel-docker" with {
    etcd "http://127.0.0.1:2379"
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource FlannelDockerScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource FlannelDockerScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource FlannelDockerScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource FlannelDockerScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource FlannelDockerScriptTest, dir, "curl.out", "${args.test.name}_curl_expected.txt"
                try {
                    assertFileResource FlannelDockerScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl_expected.txt"
                } catch (AssertionError e) {
                    try {
                        assertFileResource FlannelDockerScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl_alternative_2_expected.txt"
                    } catch (AssertionError e1) {
                    }
                }
                assertFileResource FlannelDockerScriptTest, new File(gen, '/etc/systemd/system'), "flanneld.service", "${args.test.name}_flanneld_service_expected.txt"
                assertFileResource FlannelDockerScriptTest, new File(gen, '/etc/systemd/tmpfiles.d'), "flannel.conf", "${args.test.name}_flanneld_tmpfiles_config_expected.txt"
                assertFileResource FlannelDockerScriptTest, new File(gen, '/etc/systemd/system/docker.service.d'), "10_flannel.conf", "${args.test.name}_flannel_docker_conf_expected.txt"
                assertFileResource FlannelDockerScriptTest, new File(gen, '/etc/sysconfig'), "flanneld", "${args.test.name}_flanneld_sysconfig_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_multiple_endpoints"() {
        def test = [
            name: "script_multiple_endpoints",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "flannel-docker" with {
    etcd endpoints: "https://etcd-0:2379,https://etcd-1:2379,https://etcd-2:2379"
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource FlannelDockerScriptTest, dir, "curl.out", "${args.test.name}_curl_expected.txt"
                assertFileResource FlannelDockerScriptTest, new File(gen, '/etc/sysconfig'), "flanneld", "${args.test.name}_flanneld_sysconfig_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_address_multiple_endpoints"() {
        def test = [
            name: "script_address_multiple_endpoints",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "flannel-docker" with {
    etcd address: "https://etcd-1:2379", endpoints: "https://etcd-0:2379,https://etcd-1:2379,https://etcd-2:2379"
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource FlannelDockerScriptTest, dir, "curl.out", "${args.test.name}_curl_expected.txt"
                assertFileResource FlannelDockerScriptTest, new File(gen, '/etc/sysconfig'), "flanneld", "${args.test.name}_flanneld_sysconfig_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_etcd_tls"() {
        def test = [
            name: "script_etcd_tls",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "flannel-docker" with {
    etcd "https://127.0.0.1:2379" with {
        tls certs
    }
}
''',
            scriptVars: [localhostSocket: localhostSocket, certs: testCerts],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource FlannelDockerScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource FlannelDockerScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource FlannelDockerScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource FlannelDockerScriptTest, dir, "curl.out", "${args.test.name}_curl_expected.txt"
                assertFileResource FlannelDockerScriptTest, new File(gen, '/etc/sysconfig'), "flanneld", "${args.test.name}_flanneld_sysconfig_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_nodes_targets_ufw"() {
        def test = [
            name: "script_nodes_targets_ufw",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "ssh", group: "masters" with {
    host "localhost", socket: localhostSocket
}
service "ssh", group: "nodes" with {
    host "localhost", socket: localhostSocket
}
service "flannel-docker", check: targets.default[0] with {
    node << "masters"
    node << "nodes"
    etcd "http://127.0.0.1:2379"
}
''',
            scriptVars: [localhostSocket: localhostSocket, certs: testCerts],
            before: { Map test ->
                createEchoCommand test.dir, 'which'
                createCommand ufwActiveCommand, test.dir, 'ufw'
            },
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource FlannelDockerScriptTest, dir, "ufw.out", "${args.test.name}_ufw_expected.txt"
                assertFileResource FlannelDockerScriptTest, dir, "docker.out", "${args.test.name}_docker_expected.txt"
            },
        ]
        doTest test
    }

    @BeforeEach
    void checkProfile() {
        checkProfile LOCAL_PROFILE
        checkLocalhostSocket()
    }
}
