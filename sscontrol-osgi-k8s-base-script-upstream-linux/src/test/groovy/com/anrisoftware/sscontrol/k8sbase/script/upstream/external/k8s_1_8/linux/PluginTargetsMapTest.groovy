package com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_8.linux

import javax.inject.Inject

import org.junit.Before

import com.anrisoftware.sscontrol.k8sbase.base.service.internal.EtcdPluginImpl.EtcdPluginImplFactory
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
                new K8sUpstreamModule()
                ).injectMembers(this)
    }
}
