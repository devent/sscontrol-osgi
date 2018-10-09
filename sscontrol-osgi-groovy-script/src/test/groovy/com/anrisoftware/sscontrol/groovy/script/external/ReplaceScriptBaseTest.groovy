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
class ReplaceScriptBaseTest extends AbstractScriptBaseTest {

    @Test
    void "replace Named arguments"() {
        def test = [
            name: 'replace Named arguments',
            input: new ScriptBase() {

                @Override
                Properties getDefaultProperties() {
                }

                @Override
                def run() {
                    replace "s/(?m)^define\\('DB_NAME', '.*?'\\);/define('DB_NAME', 'db');/", dest: '/var/www/wordpress/wp-config.php' call()
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
