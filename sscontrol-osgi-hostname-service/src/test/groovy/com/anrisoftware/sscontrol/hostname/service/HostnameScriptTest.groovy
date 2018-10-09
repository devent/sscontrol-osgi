package com.anrisoftware.sscontrol.hostname.service

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.hostname.service.external.Hostname
import com.anrisoftware.sscontrol.hostname.service.internal.HostnameModule
import com.anrisoftware.sscontrol.hostname.service.internal.HostnameImpl.HostnameImplFactory
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesServiceModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule
import com.anrisoftware.sscontrol.services.internal.targets.TargetsServiceModule
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.ssh.external.Ssh
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
class HostnameScriptTest {

    @Inject
    HostServicesImplFactory servicesFactory

    @Inject
    HostnameImplFactory hostnameFactory

    @Test
    void "set_fqdn"() {
        def test = [
            name: 'set_fqdn',
            input: """
service "hostname" with {
    // Sets the hostname.
    set fqdn: "blog.muellerpublic.de"
}
""",
            expected: { HostServices services ->
                assert services.getServices('hostname').size() == 1
                Hostname hostname = services.getServices('hostname')[0] as Hostname
                assert hostname.hostname == 'blog.muellerpublic.de'
            },
        ]
        doTest test
    }

    @Test
    void "fqdn"() {
        def test = [
            name: 'set_fqdn',
            input: """
service "hostname", fqdn: "blog.muellerpublic.de"
""",
            expected: { HostServices services ->
                assert services.getServices('hostname').size() == 1
                Hostname hostname = services.getServices('hostname')[0] as Hostname
                assert hostname.hostname == 'blog.muellerpublic.de'
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def services = servicesFactory.create()
        services.targets.addTarget([getGroup: {'default'}, getHosts: { []}] as Ssh)
        services.putAvailableService 'hostname', hostnameFactory
        Eval.me 'service', services, test.input as String
        Closure expected = test.expected
        expected services
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new HostnameModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new TargetsServiceModule(),
                new PropertiesModule(),
                new PropertiesUtilsModule(),
                new SystemNameMappingsModule(),
                new HostServicePropertiesServiceModule(),
                ).injectMembers(this)
    }
}
