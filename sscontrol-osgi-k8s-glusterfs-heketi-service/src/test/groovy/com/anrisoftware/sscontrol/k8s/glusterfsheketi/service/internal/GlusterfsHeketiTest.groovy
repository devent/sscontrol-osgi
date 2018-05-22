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

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.k8s.fromrepository.service.internal.FromRepositoryModule
import com.anrisoftware.sscontrol.k8s.fromrepository.service.internal.FromRepositoryImpl.FromRepositoryImplFactory
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.external.GlusterfsHeketi
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal.GlusterfsHeketiImpl.GlusterfsHeketiImplFactory
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sModule
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterFactory
import com.anrisoftware.sscontrol.k8scluster.service.internal.K8sClusterModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesServiceModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoModule
import com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoImpl.GitRepoImplFactory
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule
import com.anrisoftware.sscontrol.services.internal.targets.TargetsServiceModule
import com.anrisoftware.sscontrol.shell.external.utils.RobobeeScriptModule
import com.anrisoftware.sscontrol.shell.external.utils.SshFactory
import com.anrisoftware.sscontrol.shell.external.utils.RobobeeScript.RobobeeScriptFactory
import com.anrisoftware.sscontrol.tls.internal.TlsModule
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
import com.anrisoftware.sscontrol.utils.systemmappings.internal.SystemNameMappingsModule
import com.google.inject.Guice

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class GlusterfsHeketiTest {

    @Inject
    RobobeeScriptFactory robobeeScriptFactory

    @Inject
    K8sClusterFactory clusterFactory

    @Inject
    FromRepositoryImplFactory fromRepositoryFactory

    @Inject
    HostServicesImplFactory servicesFactory

    @Inject
    GitRepoImplFactory gitFactory

    @Inject
    GlusterfsHeketiImplFactory glusterfsHeketiFactory

    @Test
    void "json topology, implicit default cluster, implicit default nodes"() {
        def test = [
            name: 'json topology',
            script: '''
service "k8s-cluster"
service "repo-git", group: "glusterfs-heketi" with {
    remote url: "git@github.com:robobee-repos/glusterfs-heketi.git"
}
service "glusterfs-heketi", repo: "glusterfs-heketi", name: "glusterfs" with {
    admin key: "MySecret"
    user key: "MyVolumeSecret"
    brick min: 1, max: 50
    service address: "10.96.10.10"
    vars << [heketi: [snapshot: [limit: 32]]]
    vars << [tolerations: [
        [key: 'robobeerun.com/dedicated', effect: 'NoSchedule'],
        [key: 'node-role.kubernetes.io/master', effect: 'NoSchedule'],
    ]]
    topology parse: topologyJson
}
''',
            before: { },
            scriptVars: [topologyJson: topologyJson],
            expected: { HostServices services ->
                assert services.getServices('glusterfs-heketi').size() == 1
                GlusterfsHeketi s = services.getServices('glusterfs-heketi')[0]
                assert s.clusterHosts.size() == 1
                assert s.clusterHosts[0].target.host == "localhost"
                assert s.nodes.size() == 0
                assert s.minBrickSizeGb == 1
                assert s.maxBrickSizeGb == 50
                assert s.serviceAddress == "10.96.10.10"
                assert s.repo.repo.group == "glusterfs-heketi"
                assert s.labelName == 'glusterfs'
                assert s.vars.size() == 2
                assert s.vars.tolerations[0].key == "robobeerun.com/dedicated"
                assert s.vars.tolerations[0].effect == "NoSchedule"
                assert s.vars.tolerations[1].key == "node-role.kubernetes.io/master"
                assert s.vars.tolerations[1].effect == "NoSchedule"
                assert s.admin.key == 'MySecret'
                assert s.user.key == 'MyVolumeSecret'
                assert s.topology.size() == 1
            },
        ]
        doTest test
    }

    def topologyJson = """
{
  "clusters":[
    {
      "nodes":[
        {
          "node":{
            "hostnames":{
              "manage":[
                "node0"
              ],
              "storage":[
                "192.168.10.100"
              ]
            },
            "zone":1
          },
          "devices":[
            "/dev/vdb",
            "/dev/vdc",
            "/dev/vdd"
          ]
        },
        {
          "node":{
            "hostnames":{
              "manage":[
                "node1"
              ],
              "storage":[
                "192.168.10.101"
              ]
            },
            "zone":1
          },
          "devices":[
            "/dev/vdb",
            "/dev/vdc",
            "/dev/vdd"
          ]
        },
        {
          "node":{
            "hostnames":{
              "manage":[
                "node2"
              ],
              "storage":[
                "192.168.10.102"
              ]
            },
            "zone":1
          },
          "devices":[
            "/dev/vdb",
            "/dev/vdc",
            "/dev/vdd"
          ]
        }
      ]
    }
  ]
}
"""

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        test.before(test)
        def services = servicesFactory.create()
        services.targets.addTarget SshFactory.localhost(injector)
        services.putAvailableService 'k8s-cluster', clusterFactory
        services.putAvailableService 'repo-git', gitFactory
        services.putAvailableService 'from-repository', fromRepositoryFactory
        services.putAvailableService 'glusterfs-heketi', glusterfsHeketiFactory
        robobeeScriptFactory.create folder.newFile(), test.script, test.scriptVars, services call()
        Closure expected = test.expected
        expected services
    }

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    def injector

    @Before
    void setupTest() {
        toStringStyle
        injector = Guice.createInjector(
                new K8sModule(),
                new K8sClusterModule(),
                new FromRepositoryModule(),
                new GitRepoModule(),
                new GlusterfsHeketiModule(),
                new PropertiesModule(),
                new DebugLoggingModule(),
                new TypesModule(),
                new StringsModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new TargetsServiceModule(),
                new PropertiesUtilsModule(),
                new ResourcesModule(),
                new TlsModule(),
                new RobobeeScriptModule(),
                new SystemNameMappingsModule(),
                new HostServicePropertiesServiceModule(),
                )
        injector.injectMembers(this)
    }
}
