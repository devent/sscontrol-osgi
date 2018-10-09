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
class FetchScriptBaseTest extends AbstractScriptBaseTest {

    @Test
    void "fetch basic"() {
        def test = [
            name: 'fetch basic',
            input: new ScriptBase() {

                @Override
                Properties getDefaultProperties() {
                }

                @Override
                def run() {
                    fetch '/etc/postfix/main.cf' call()
                }

                @Override
                String getSystemName() {
                    ''
                }

                @Override
                String getSystemVersion() {
                    ''
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
