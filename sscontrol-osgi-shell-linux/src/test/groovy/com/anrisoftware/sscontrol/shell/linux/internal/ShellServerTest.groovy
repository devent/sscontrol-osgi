package com.anrisoftware.sscontrol.shell.linux.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class ShellServerTest extends AbstractShellRunnerTestBase {

    static final URL robobeeKey = UnixTestUtil.class.getResource('robobee')

    static final URL wordpressZip = ShellServerTest.class.getResource('wordpress-app.zip')

    @Test
    void "git_file"() {
        def test = [
            name: "git_file",
            script: """
service "ssh", host: "robobee@robobee-test", key: "$robobeeKey"
service "repo-git", group: 'wordpress-app' with {
    remote url: "/tmp/wordpress-app"
    property << "checkout_directory=\$checkoutDir"
}
""",
            scriptVars: [checkoutDir: '/tmp/w'],
            expectedServicesSize: 2,
            before: { Map test ->
                execRemoteFile """
if ! which unzip; then
sudo DEBIAN_FRONTEND=noninteractive apt-get update
sudo DEBIAN_FRONTEND=noninteractive apt-get install -y unzip
fi
mkdir -p /tmp/wordpress-app
cd /tmp/wordpress-app
cat > wordpress-app.zip
unzip wordpress-app.zip
""", wordpressZip.openStream()
            },
            after: { Map test -> remoteCommand """
rm -rf "${test.scriptVars.checkoutDir}"
rm -r /tmp/wordpress-app
""" },
            expected: { Map args ->
                assertStringResource ShellServerTest, readRemoteFile(new File(args.test.scriptVars.checkoutDir, 'mysql-deployment.yaml').absolutePath), "${args.test.name}_mysql_deployment_yaml_expected.txt"
                assertStringResource ShellServerTest, readRemoteFile(new File(args.test.scriptVars.checkoutDir, 'wordpress-deployment.yaml').absolutePath), "${args.test.name}_wordpress_deployment_yaml_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "git_ssh"() {
        def test = [
            name: "git_file",
            script: """
service "ssh", host: "robobee@robobee-test", key: "$robobeeKey"
service "repo-git", group: 'wordpress-app' with {
    remote url: "git@github.com:robobee-repos/wordpress-app-test.git"
    credentials "ssh", key: "$robobeeKey"
    property << "checkout_directory=\$checkoutDir"
}
""",
            scriptVars: [checkoutDir: '/tmp/w'],
            expectedServicesSize: 2,
            after: { Map test -> remoteCommand """
rm -rf "${test.scriptVars.checkoutDir}"
""" },
            expected: { Map args ->
                assertStringResource ShellServerTest, readRemoteFile(new File(args.test.scriptVars.checkoutDir, 'mysql-deployment.yaml').absolutePath), "${args.test.name}_mysql_deployment_yaml_expected.txt"
                assertStringResource ShellServerTest, readRemoteFile(new File(args.test.scriptVars.checkoutDir, 'wordpress-deployment.yaml').absolutePath), "${args.test.name}_wordpress_deployment_yaml_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "git_ssh_tag"() {
        def test = [
            name: "git_ssh_tag",
            script: """
service "ssh", host: "robobee@robobee-test", key: "$robobeeKey"
service "repo-git", group: 'wordpress-app' with {
    remote url: "git@github.com:robobee-repos/wordpress-app-test.git"
    checkout tag: "st"
    credentials "ssh", key: "$robobeeKey"
    property << "checkout_directory=\$checkoutDir"
}
""",
            scriptVars: [checkoutDir: '/tmp/w'],
            expectedServicesSize: 2,
            after: { Map test -> remoteCommand """
rm -rf "${test.scriptVars.checkoutDir}"
""" },
            expected: { Map args ->
                assertStringResource ShellServerTest, readRemoteFile(new File(args.test.scriptVars.checkoutDir, 'mysql-deployment-yaml.st').absolutePath), "${args.test.name}_mysql_deployment_yaml_st_expected.txt"
                assertStringResource ShellServerTest, readRemoteFile(new File(args.test.scriptVars.checkoutDir, 'wordpress-deployment-yaml.st').absolutePath), "${args.test.name}_wordpress_deployment_yaml_st_expected.txt"
            },
        ]
        doTest test
    }

    @Before
    void beforeMethod() {
        assumeTrue testHostAvailable
    }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv args
    }

    void createDummyCommands(File dir) {
    }

    def setupServiceScript(Map args, HostServiceScript script) {
        return script
    }
}
