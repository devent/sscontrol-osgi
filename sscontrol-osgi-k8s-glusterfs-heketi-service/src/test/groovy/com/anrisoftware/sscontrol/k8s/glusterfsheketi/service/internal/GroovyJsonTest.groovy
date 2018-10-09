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
