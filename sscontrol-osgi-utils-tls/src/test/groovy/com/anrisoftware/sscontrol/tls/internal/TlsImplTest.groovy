package com.anrisoftware.sscontrol.tls.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.sscontrol.tls.external.Tls
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory
import com.google.inject.Guice

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class TlsImplTest {

    @Inject
    TlsFactory tlsFactory

    @Test
    void "tls"() {
        def test = [
            name: 'cluster range',
            input: """
tls ca: "ca.pem", cert: "cert.pem", key: "key.pem"
""",
            expected: { Tls tls ->
                assert tls.ca.toString() == 'file:ca.pem'
                assert tls.cert.toString() == 'file:cert.pem'
                assert tls.key.toString() == 'file:key.pem'
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tls = Eval.me 'tls', { Map args ->
            tlsFactory.create args
        }, test.input as String
        Closure expected = test.expected
        expected tls
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new TlsModule(),
                new ResourcesModule()
                ).injectMembers(this)
    }
}
