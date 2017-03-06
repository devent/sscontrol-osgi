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
package com.anrisoftware.sscontrol.rkt.debian.internal.rkt_1_25_debian_8

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
class Rkt_Debian_Test extends AbstractTest_Rkt_Debian {

    @Test
    void "rkt"() {
        def test = [
            name: "tls_etcd_target",
            input: """
service "ssh", host: "localhost"
service "rkt-1.25"
""",
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource Rkt_Debian_Test, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource Rkt_Debian_Test, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                //assertFileResource K8sMaster_1_5_Debian_8_Test, dir, "systemctl.out", "${args.test.name}_systemctl_expected.txt"
                assertFileResource Rkt_Debian_Test, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource Rkt_Debian_Test, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource Rkt_Debian_Test, new File(gen, '/etc/systemd/system'), "kube-apiserver.service", "${args.test.name}_kube_apiserver_service_expected.txt"
                assertFileResource Rkt_Debian_Test, new File(gen, '/etc/systemd/system'), "kube-controller-manager.service", "${args.test.name}_kube_controller_manager_service_expected.txt"
                assertFileResource Rkt_Debian_Test, new File(gen, '/etc/systemd/system'), "kube-scheduler.service", "${args.test.name}_kube_scheduler_service_expected.txt"
                assertFileResource Rkt_Debian_Test, new File(gen, '/etc/systemd/tmpfiles.d'), "kubernetes.conf", "${args.test.name}_kubernetes_conf_expected.txt"
                assertFileResource Rkt_Debian_Test, new File(gen, '/etc/kubernetes'), "config", "${args.test.name}_kubernetes_config_config_expected.txt"
                assertFileResource Rkt_Debian_Test, new File(gen, '/etc/kubernetes'), "apiserver", "${args.test.name}_kubernetes_apiserver_config_expected.txt"
                assertFileResource Rkt_Debian_Test, new File(gen, '/etc/kubernetes'), "controller-manager", "${args.test.name}_kubernetes_controller_manager_config_expected.txt"
                assertFileResource Rkt_Debian_Test, new File(gen, '/etc/kubernetes'), "scheduler", "${args.test.name}_kubernetes_scheduler_config_expected.txt"
            },
        ]
        doTest test
    }
}
