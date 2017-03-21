package com.anrisoftware.sscontrol.k8s.upstream.external

import javax.inject.Inject

import org.junit.Before

import com.anrisoftware.sscontrol.k8s.upstream.external.K8s_1_5_Upstream_Module
import com.anrisoftware.sscontrol.k8smaster.internal.EtcdPluginImpl.EtcdPluginImplFactory
import com.anrisoftware.sscontrol.k8smaster.upstream.external.PluginTargetsMap.PluginTargetsMapFactory
import com.google.inject.Guice

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class PluginTargetsMapTest {

    @Inject
    PluginTargetsMapFactory pluginTargetsMapFactory

    @Inject
    EtcdPluginImplFactory etcdPluginFactory

    //@Test
    void "get etcd"() {
        def map = [
            "etcd": etcdPluginFactory.create([target: 'etcd0', protocol: 'http', port: 2379])
        ]
        etcdPluginFactory
    }

    @Before
    void injectDependencies() {
        Guice.createInjector(
                new K8s_1_5_Upstream_Module()
                ).injectMembers(this)
    }
}
