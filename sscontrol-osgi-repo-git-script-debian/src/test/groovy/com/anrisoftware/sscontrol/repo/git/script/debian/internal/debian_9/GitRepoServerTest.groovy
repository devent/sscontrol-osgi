package com.anrisoftware.sscontrol.repo.git.script.debian.internal.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.jupiter.api.Assumptions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class GitRepoServerTest extends AbstractGitRunnerTest {

    static final URL wordpressZip = GitRepoServerTest.class.getResource('wordpress-app_zip.txt')

    @Test
    void "server_git_file"() {
        def test = [
            name: "server_git_file",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "repo-git", group: 'wordpress-app' with {
    remote url: "/tmp/wordpress-app"
    property << "checkout_directory=${checkoutDir}"
}
''',
            scriptVars: [checkoutDir: '/tmp/w', robobeeSocket: robobeeSocket],
            expectedServicesSize: 2,
            before: { Map test ->
                execRemoteFile """
set -ex
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
set -ex
rm -rf "${test.scriptVars.checkoutDir}"
rm -r /tmp/wordpress-app
""" },
            expected: { Map args ->
                Map scriptVars = args.test.scriptVars
                assertStringResource GitRepoServerTest, readRemoteFile(new File(scriptVars.checkoutDir, 'mysql-deployment.yaml').absolutePath), "${args.test.name}_mysql_deployment_yaml_expected.txt"
                assertStringResource GitRepoServerTest, readRemoteFile(new File(scriptVars.checkoutDir, 'wordpress-deployment.yaml').absolutePath), "${args.test.name}_wordpress_deployment_yaml_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "server_git_ssh"() {
        def test = [
            name: "server_git_ssh",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "repo-git", group: "wordpress-app" with {
    remote url: "git@github.com:robobee-repos/wordpress-app-test.git"
    credentials "ssh", key: robobeeKey
    property << "checkout_directory=${checkoutDir}"
}
''',
            scriptVars: [checkoutDir: '/tmp/w', robobeeSocket: robobeeSocket, robobeeKey: robobeeKey],
            expectedServicesSize: 2,
            after: { Map test -> remoteCommand """
set -ex
rm -rf "${test.scriptVars.checkoutDir}"
""" },
            expected: { Map args ->
                assertStringResource GitRepoServerTest, readRemoteFile(new File(args.test.scriptVars.checkoutDir, 'mysql-deployment.yaml').absolutePath), "${args.test.name}_mysql_deployment_yaml_expected.txt"
                assertStringResource GitRepoServerTest, readRemoteFile(new File(args.test.scriptVars.checkoutDir, 'wordpress-deployment.yaml').absolutePath), "${args.test.name}_wordpress_deployment_yaml_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "server_git_ssh_tag"() {
        def test = [
            name: "server_git_ssh_tag",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "repo-git", group: "wordpress-app" with {
    remote url: "git@github.com:robobee-repos/wordpress-app-test.git"
    checkout tag: "st"
    credentials "ssh", key: robobeeKey
    property << "checkout_directory=${checkoutDir}"
}
''',
            scriptVars: [checkoutDir: '/tmp/w', robobeeSocket: robobeeSocket, robobeeKey: robobeeKey],
            expectedServicesSize: 2,
            after: { Map test -> remoteCommand """
set -ex
rm -rf "${test.scriptVars.checkoutDir}"
""" },
            expected: { Map args ->
                assertStringResource GitRepoServerTest, readRemoteFile(new File(args.test.scriptVars.checkoutDir, 'mysql-deployment-yaml.st').absolutePath), "${args.test.name}_mysql_deployment_yaml_st_expected.txt"
                assertStringResource GitRepoServerTest, readRemoteFile(new File(args.test.scriptVars.checkoutDir, 'wordpress-deployment-yaml.st').absolutePath), "${args.test.name}_wordpress_deployment_yaml_st_expected.txt"
            },
        ]
        doTest test
    }

    @BeforeEach
    void beforeMethod() {
        checkRobobeeSocket()
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
