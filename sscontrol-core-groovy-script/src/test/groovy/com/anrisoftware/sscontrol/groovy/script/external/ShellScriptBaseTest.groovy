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
package com.anrisoftware.sscontrol.groovy.script.external

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import org.junit.jupiter.api.Test

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class ShellScriptBaseTest extends AbstractScriptBaseTest {

    @Test
    void "shell basic"() {
        def test = [
            name: 'shell basic',
            input: new AbstractDefaultScriptBase() {

                @Override
                def run() {
                    shell "echo 'test shell'" call()
                }
            },
            expected: {
            }
        ]
        log.info '{} --- case: {}', test.name, test
        def script = createScript(test)
        createEchoCommands script.chdir, ['sudo']
        script.run()
        Closure expected = test.expected
        expected()
    }

    @Test
    void "shell env x=y"() {
        def test = [
            name: 'shell env x=y',
            input: new AbstractDefaultScriptBase() {

                @Override
                @CompileDynamic
                def run() {
                    shell "echo \"test shell \$STRING\"" with { //
                        env "STRING=hello" } call()
                }
            },
            expected: {
            }
        ]
        log.info '{} --- case: {}', test.name, test
        def script = createScript(test)
        createEchoCommands script.chdir, ['sudo']
        script.run()
        Closure expected = test.expected
        expected()
    }

    @Test
    void "shell env single quote"() {
        def test = [
            name: 'shell env single quote',
            input: new AbstractDefaultScriptBase() {

                @Override
                @CompileDynamic
                def run() {
                    shell "echo \"test shell \$STRING\"" with { //
                        env "STRING='hello'" } call()
                }
            },
            expected: {
            }
        ]
        log.info '{} --- case: {}', test.name, test
        def script = createScript(test)
        createEchoCommands script.chdir, ['sudo']
        script.run()
        Closure expected = test.expected
        expected()
    }

    @Test
    void "shell env var expansion"() {
        def test = [
            name: 'shell env var expansion',
            input: new AbstractDefaultScriptBase() {

                @Override
                @CompileDynamic
                def run() {
                    shell "echo \"test shell \$STRING\"" with { //
                        env "STRING=\"hello \$HOSTNAME\"" } call()
                }
            },
            expected: {
            }
        ]
        log.info '{} --- case: {}', test.name, test
        def script = createScript(test)
        createEchoCommands script.chdir, ['sudo']
        script.run()
        Closure expected = test.expected
        expected()
    }

    @Test
    void "shell env args expansion"() {
        def test = [
            name: 'shell env args expansion',
            input: new AbstractDefaultScriptBase() {

                @Override
                @CompileDynamic
                def run() {
                    shell "echo \"test shell \$STRING\"" with {
                        env name: "STRING", value: "hello world"
                    } call()
                }
            },
            expected: {
            }
        ]
        log.info '{} --- case: {}', test.name, test
        def script = createScript(test)
        createEchoCommands script.chdir, ['sudo']
        script.run()
        Closure expected = test.expected
        expected()
    }

    @Test
    void "shell env args no expansion"() {
        def test = [
            name: 'shell env args no expansion',
            input: new AbstractDefaultScriptBase() {

                @Override
                @CompileDynamic
                def run() {
                    shell "echo \"test shell \$STRING\"" with {
                        env name: "STRING", value: "hello \$HOSTNAME", literally: false
                    } call()
                }
            },
            expected: {
            }
        ]
        log.info '{} --- case: {}', test.name, test
        def script = createScript(test)
        createEchoCommands script.chdir, ['sudo']
        script.run()
        Closure expected = test.expected
        expected()
    }
}
