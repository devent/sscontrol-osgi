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
package com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal

import org.junit.Test

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class GroovyJsonTest {

    @Test
    void "parse topology"() {
        def jsonSlurper = new JsonSlurper()
        Map<String, Object> object = jsonSlurper.parseText '''
{
  "clusters": [
    {
      "nodes": [
        {
          "node": {
            "hostnames": {
              "manage": [
                "node0"
              ],
              "storage": [
                "192.168.10.100"
              ]
            },
            "zone": 1
          },
          "devices": [
            "/dev/vdb",
            "/dev/vdc",
            "/dev/vdd"
          ]
        },
        {
          "node": {
            "hostnames": {
              "manage": [
                "node1"
              ],
              "storage": [
                "192.168.10.101"
              ]
            },
            "zone": 1
          },
          "devices": [
            "/dev/vdb",
            "/dev/vdc",
            "/dev/vdd"
          ]
        },
        {
          "node": {
            "hostnames": {
              "manage": [
                "node2"
              ],
              "storage": [
                "192.168.10.102"
              ]
            },
            "zone": 1
          },
          "devices": [
            "/dev/vdb",
            "/dev/vdc",
            "/dev/vdd"
          ]
        }
      ]
    }
  ]
}
'''
        assert object instanceof Map
        assert object.size() == 1
        assert object.clusters.size() == 1
        assert object.clusters.nodes.size() == 1
        assert object.clusters.nodes[0].node.size() == 3

        assert JsonOutput.toJson(object) == '{"clusters":[{"nodes":[{"devices":["/dev/vdb","/dev/vdc","/dev/vdd"],"node":{"hostnames":{"manage":["node0"],"storage":["192.168.10.100"]},"zone":1}},{"devices":["/dev/vdb","/dev/vdc","/dev/vdd"],"node":{"hostnames":{"manage":["node1"],"storage":["192.168.10.101"]},"zone":1}},{"devices":["/dev/vdb","/dev/vdc","/dev/vdd"],"node":{"hostnames":{"manage":["node2"],"storage":["192.168.10.102"]},"zone":1}}]}]}'
    }
}
