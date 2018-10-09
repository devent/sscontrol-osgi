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
