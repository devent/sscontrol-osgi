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
package com.anrisoftware.sscontrol.groovy.script.external

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import org.junit.Test

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class CopyScriptBaseTest extends AbstractScriptBaseTest {

    @Test
    void "copy Named arguments syntax"() {
        def test = [
            name: 'copy Named arguments syntax.',
            input: new ScriptBase() {

                @Override
                Properties getDefaultProperties() {
                }

                @Override
                def run() {
                    copy src: 'main.cf', dest: '/etc/postfix/main.cf' call()
                }

                @Override
                String getSystemName() {
                    ''
                }

                @Override
                String getSystemVersion() {
                    ''
                }

                @Override
                Boolean getArchiveIgnoreKey() {
                    false
                }
            },
            expected: {
            }
        ]
        log.info '{} --- case: {}', test.name, test
        def script = createScript(test)
        createCommands test
        script.run()
        Closure expected = test.expected
        expected()
    }

    @Test
    void "copy Short syntax"() {
        def test = [
            name: 'copy Short syntax.',
            input: new ScriptBase() {

                @Override
                Properties getDefaultProperties() {
                }

                @Override
                def run() {
                    copy 'main.cf', dest: '/etc/postfix/main.cf' call()
                }

                @Override
                String getSystemName() {
                    ''
                }

                @Override
                String getSystemVersion() {
                    ''
                }

                @Override
                Boolean getArchiveIgnoreKey() {
                    false
                }
            },
            expected: {
            }
        ]
        log.info '{} --- case: {}', test.name, test
        def script = createScript(test)
        createCommands test
        script.run()
        Closure expected = test.expected
        expected()
    }

    def createCommands(Map test) {
        createEchoCommands test.tmp, [
            'chown',
            'chmod',
            'sudo',
            'scp',
            'rm',
            'cp',
        ]
    }
}
