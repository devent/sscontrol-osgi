/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.runner.groovy.internal

service "ssh", group: "master" with {
    //.
    host "robobee@andrea-master", key: K8sScript.class.getResource("robobee")
}

service "ssh", group: "nodes" with {
    //.
    host "robobee@andrea-node-1", key: K8sScript.class.getResource("robobee")
}

service "hostname", target: "master", fqdn: "andrea-master.muellerpublic.de"

service "hosts", target: "master" with {
    ip "185.24.220.41", host: "andrea-master.muellerpublic.de", alias: "andrea-master"
    ip "37.252.124.149", host: "andrea-node-1.muellerpublic.de", alias: "andrea-node-1"
}

service "hostname", target: "nodes", fqdn: "andrea-node-1.muellerpublic.de"

service "hosts", target: "nodes" with {
    ip "37.252.124.149", host: "andrea-node-1.muellerpublic.de", alias: "andrea-node-1"
    ip "185.24.220.41", host: "andrea-master.muellerpublic.de", alias: "andrea-master"
}
