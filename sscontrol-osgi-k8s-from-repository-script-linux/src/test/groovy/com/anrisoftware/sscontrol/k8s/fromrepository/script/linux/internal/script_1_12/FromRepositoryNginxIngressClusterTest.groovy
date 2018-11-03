/*-
 * #%L
 * sscontrol-osgi - k8s-from-repository-script-linux
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_12

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.shell.external.utils.Nodes3AvailableCondition.*
import static org.junit.jupiter.api.Assumptions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import com.anrisoftware.sscontrol.shell.external.utils.Nodes3AvailableCondition
import com.anrisoftware.sscontrol.shell.external.utils.RobobeeSocketCondition
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@ExtendWith(Nodes3AvailableCondition.class)
class FromRepositoryNginxIngressClusterTest extends AbstractFromRepositoryRunnerTest {

    @Test
    void "nginx ingress addon to cluster"() {
        def test = [
            name: "cluster_nginx_ingress_addon_cluster",
            script: '''
def edgeNodeTargetHttpPort = 30000
def edgeNodeTargetHttpsPort = 30001
def edgeNodeTargetSshPort = 30022

service "ssh" with {
    host "robobee@node-0.robobee-test.test", socket: sockets.node[0]
}
service "ssh", group: "edge-nodes" with {
    host "robobee@node-1.robobee-test.test", socket: sockets.node[1]
}
service "k8s-cluster" with {
}
service "repo-git", group: "nginx-ingress" with {
    remote url: "git@github.com:robobee-repos/nginx-ingress.git"
    credentials "ssh", key: robobeeKey
}
service "from-repository", repo: "nginx-ingress", dest: "/etc/kubernetes/addons/ingress-nginx" with {
    vars << [
        nginxIngressController: [
            image: [name: 'quay.io/kubernetes-ingress-controller/nginx-ingress-controller', version: '0.14.0'],
            limits: [cpu: '0', memory: '200Mi'],
            requests: [cpu: '0', memory: '200Mi'],
            affinity: [key: "robobeerun.com/ingress-nginx", name: "required", required: true],
            nodePort: [http: edgeNodeTargetHttpPort, https: edgeNodeTargetHttpsPort],
            config: [
                useProxyProtocol: true,
            ]
        ]
    ]
    vars << [
        defaultHttpBackend: [
            image: [name: 'gcr.io/google_containers/defaultbackend', version: '1.4'],
            limits: [cpu: '10m', memory: '20Mi'],
            requests: [cpu: '10m', memory: '20Mi'],
            affinity: [key: "robobeerun.com/ingress-nginx", name: "required", required: true],
        ]
    ]

def edgeNodeTargetAddress = targets["edge-nodes"][0].hostAddress

service "shell", target: "edge-nodes" with {
    script timeout: "PT10M", privileged: true, """
if systemctl list-units | grep haproxy.service | grep running; then
systemctl stop haproxy
fi
"""
}

service "shell", target: "edge-nodes" with {
    script timeout: "PT1H", privileged: true, """
echo "deb http://deb.debian.org/debian stretch-backports main" > /etc/apt/sources.list.d/backports.list
export DEBIAN_FRONTEND=noninteractive
apt-get -y update
apt-get -y -t stretch-backports install haproxy
"""
    script privileged: true, """
cat <<'EOF' > /etc/haproxy/haproxy.cfg
${IOUtils.toString(haproxyDefaultConfig, "UTF-8")}
EOF
cat <<'EOF' >> /etc/haproxy/haproxy.cfg
frontend http-in
    bind *:80
    mode http
    redirect scheme https code 301 unless { path_beg /.well-known/acme-challenge/ }
    default_backend nodes-http
backend nodes-http
    mode http
    balance roundrobin
    option forwardfor
    http-request set-header X-Forwarded-Port %[dst_port]
    http-request add-header X-Forwarded-Proto https if { ssl_fc }
    option httpchk HEAD / HTTP/1.1\\\\r\\\\nHost:\\\\ 37.252.124.149:30000
    http-check expect rstatus ((2|3)[0-9][0-9]|404)
    server ingress01 ${edgeNodeTargetAddress}:${edgeNodeTargetHttpPort} check send-proxy
EOF
cat <<'EOF' >> /etc/haproxy/haproxy.cfg
frontend https-in
    bind            ${edgeNodeTargetAddress}:443 name andrea-node-1
    mode            tcp
    option          tcplog
    tcp-request     inspect-delay 5s
    tcp-request     content accept if { req.ssl_hello_type 1 }
    acl proto_tls   req.ssl_hello_type 1
    use_backend nodes-https if proto_tls
    default_backend nodes-https
backend nodes-https
    mode            tcp
    log             global
    stick-table     type ip size 512k expire 30m
    stick on        dst
    balance         roundrobin
    server          ingress01-https ${edgeNodeTargetAddress}:${edgeNodeTargetHttpsPort} check inter 1000 send-proxy

EOF
cat <<'EOF' >> /etc/haproxy/haproxy.cfg
frontend ssh-in
    bind            ${edgeNodeTargetAddress} name andrea-node-1
    mode            tcp
    default_backend nodes-ssh
backend nodes-ssh
    mode            tcp
    log             global
    stick-table     type ip size 512k expire 30m
    stick on        dst
    balance         roundrobin
    retries         3
    option          tcplog
    server          ingress01-ssh ${edgeNodeTargetAddress}:${edgeNodeTargetSshPort} check inter 1000
EOF
cat <<'EOF' >> /etc/haproxy/haproxy.cfg
listen stats
    bind ${edgeNodeTargetAddress}:9000
    mode http
    stats enable
    stats realm Haproxy\\\\ Statistics
    stats uri /
    stats auth oot1Yaok:cah8Eeka

EOF
"""
}
''',
            scriptVars: [sockets: nodesSockets, robobeeKey: robobeeKey],
            expectedServicesSize: 4,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv args
    }

    void createDummyCommands(File dir) {
    }

    def setupServiceScript(Map args, HostServiceScript script) {
        return script
    }
}
