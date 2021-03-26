/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.tls.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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

    @BeforeEach
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new TlsModule(),
                new ResourcesModule()
                ).injectMembers(this)
    }
}
