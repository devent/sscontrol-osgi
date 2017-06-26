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

import static org.apache.commons.io.FilenameUtils.*
import static org.apache.commons.lang3.Validate.*
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import java.nio.charset.Charset
import java.util.concurrent.ExecutorService

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.builder.ToStringBuilder
import org.joda.time.Duration
import org.stringtemplate.v4.ST

import com.anrisoftware.globalpom.core.durationformat.DurationFormat
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.command.copy.external.Copy
import com.anrisoftware.sscontrol.command.copy.external.Copy.CopyFactory
import com.anrisoftware.sscontrol.command.facts.external.Facts
import com.anrisoftware.sscontrol.command.facts.external.Facts.FactsFactory
import com.anrisoftware.sscontrol.command.fetch.external.Fetch
import com.anrisoftware.sscontrol.command.fetch.external.Fetch.FetchFactory
import com.anrisoftware.sscontrol.command.replace.external.Replace
import com.anrisoftware.sscontrol.command.replace.external.Replace.ReplaceFactory
import com.anrisoftware.sscontrol.command.shell.external.Shell
import com.anrisoftware.sscontrol.command.shell.external.Shell.ShellFactory
import com.anrisoftware.sscontrol.template.external.Template
import com.anrisoftware.sscontrol.template.external.Template.TemplateFactory
import com.anrisoftware.sscontrol.types.host.external.HostService
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript
import com.anrisoftware.sscontrol.types.host.external.HostServiceScriptService
import com.anrisoftware.sscontrol.types.host.external.HostServiceService
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.host.external.SystemInfo
import com.anrisoftware.sscontrol.types.host.external.TargetHost
import com.anrisoftware.sscontrol.types.ssh.external.SshHost
import com.anrisoftware.sscontrol.utils.systemmappings.external.AbstractScriptInfo
import com.google.inject.assistedinject.Assisted

import groovy.util.logging.Slf4j

/**
 * Base of all scripts that provides utilities functions and basic properties.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class ScriptBase extends Script implements HostServiceScript {

    /**
     * The {@link String} name of the script.
     */
    String name

    /**
     * The {@link HostService} service.
     */
    @Inject
    @Assisted
    HostService service

    /**
     * The {@link ExecutorService} pool to run the scripts on.
     */
    @Inject
    @Assisted
    ExecutorService threads

    /**
     * The {@link HostServices} containing the services.
     */
    @Inject
    @Assisted
    HostServices scriptsRepository

    /**
     * The hosts targets.
     */
    @Inject
    @Assisted
    TargetHost target

    /**
     * Shell command.
     */
    @Inject
    ShellFactory shell

    /**
     * Fetch service.
     */
    @Inject
    FetchFactory fetch

    /**
     * Copy service.
     */
    @Inject
    CopyFactory copy

    /**
     * Replace service.
     */
    @Inject
    ReplaceFactory replace

    /**
     * Template service.
     */
    @Inject
    TemplateFactory template

    /**
     * Facts service.
     */
    @Inject
    FactsFactory facts

    /**
     * The host services.
     */
    HostServices services

    /**
     * The current working directory.
     */
    File pwd

    /**
     * The directory from where scripts are run.
     */
    File chdir

    /**
     * The directory from where sudo is run.
     */
    File sudoChdir

    /**
     * The base directory for the script.
     */
    File base

    /**
     * Environment variables.
     */
    Map env = [:]

    /**
     * Environment variables for sudo.
     */
    Map sudoEnv = [:]

    /**
     * Script environment.
     */
    Map scriptEnv

    /**
     * Creates a temporary file.
     */
    def createTmpFileCallback

    /**
     * Returns to copy and replace local files for tests.
     */
    def filesLocal

    @Inject
    void setScriptEnv(@Assisted Map<String, Object> scriptEnv) {
        this.scriptEnv = new HashMap(scriptEnv)
        this.pwd = scriptEnv.pwd
        this.chdir = scriptEnv.chdir
        this.base = scriptEnv.base
        this.sudoChdir = scriptEnv.sudoChdir
        this.env = scriptEnv.env
        this.sudoEnv = scriptEnv.sudoEnv
        this.createTmpFileCallback = scriptEnv.createTmpFileCallback
        this.filesLocal = scriptEnv.filesLocal
    }

    @Override
    public <T extends ExecutorService> T getThreads() {
        threads
    }

    @Override
    SshHost getTarget() {
        target
    }

    @Override
    def getLog() {
        log
    }

    @Override
    HostServiceProperties getProperties() {
        def p = service.serviceProperties
        notNull p, "serviceProperties=null for $service"
        return p
    }

    @Override
    String toString() {
        new ToStringBuilder(this).
                append('name', getName()).
                append('system', getSystemName()).
                append('version', getSystemVersion()).
                toString()
    }

    /**
     * Executes the command that is created from the specified factory.
     *
     * @param args
     * the {@link Map} command arguments.
     * @param factory
     * the command factory.
     */
    def createCmd(Map args, def factory) {
        def a = setupArgs(args, 'shell')
        factory.create(a, a.target, a.parent, threads, log)
    }

    /**
     * Shell command.
     */
    Shell shell(String command) {
        shell([:], command)
    }

    /**
     * Shell command.
     */
    Shell shell(Map args, String command) {
        def a = new HashMap(args)
        a.command = command
        shell a
    }

    /**
     * Shell command.
     */
    Shell shell(Map args) {
        def a = setupArgs(args, 'shell')
        shell.create(a, a.target, a.parent, threads, log)
    }

    /**
     * Fetch command.
     */
    Fetch fetch(String src) {
        fetch([src: src])
    }

    /**
     * Fetch command.
     */
    Fetch fetch(Map args) {
        def a = setupArgs(args, 'fetch')
        log.debug 'Fetch file {}', a
        fetch.create(a, a.target, this, threads, log)
    }

    /**
     * Copy command.
     * <p>
     * <pre>
     * copy dest: '/etc/config' call()
     * </pre>
     * </p>
     */
    Copy copy(Map args, String src) {
        def a = new HashMap(args)
        a.src = src
        copy(a)
    }

    /**
     * Copy command.
     */
    Copy copy(Map args) {
        def a = setupArgs(args, 'copy')
        if (archiveIgnoreKey) {
            a.sig = null
            a.server = null
            a.key = null
        }
        log.debug 'Copy file {}', a
        copy.create(a, a.target, this, threads, log)
    }

    /**
     * Replace command.
     * <pre>
     * replace privileged: true, dest: '/etc/config' with {
     * line "s&sol;(?m)^foo.*&sol;foo=bar&sol;"
     *        it
     *    }()
     * </pre>
     */
    Replace replace(Map args, String search) {
        def a = new HashMap(args)
        a.search = search
        replace(a)
    }

    /**
     * Replace command.
     */
    Replace replace(Map args) {
        def a = setupArgs(args, 'replace')
        replace.create(a, a.target, this, threads, log)
    }

    /**
     * Template command.
     */
    Template template(Map args, String template) {
        def a = new HashMap(args)
        a.template = template
        template(a)
    }

    /**
     * Template command.
     * <p>
     * <pre>
     * template base: 'Base_Templates', resource: 'foo_template', name: 'foo', vars: [:], dest: '/etc/config' call()
     * template resource: fooTemplateResource, name: 'foo', vars: [:], dest: '/etc/config' call()
     * </pre>
     * </p>
     */
    Template template(Map args) {
        def a = setupArgs(args, 'template')
        template.create(a, a.target, this, threads, log)
    }

    /**
     * Check a status of a command.
     * @param command the command which status is checked, for example
     * <code>"uname -a | grep '4.0.0'"</code>
     */
    boolean check(String command) {
        check([command: command])
    }

    /**
     * Check a status of a command.
     * @param args <i>command</i> the command which status is checked, for example
     * <code>uname -a | grep '4.0.0'</code>
     */
    boolean check(Map args) {
        assertThat "command=null", args.command, not(isEmptyOrNullString())
        def a = new HashMap(args)
        a.exitCodes = [0, 1] as int[]
        def r = shell a call()
        return r.exitValue == 0
    }

    /**
     * Facts command.
     */
    Facts facts() {
        facts([:])
    }

    /**
     * Facts command.
     */
    Facts facts(Map args) {
        def a = setupArgs(args, 'facts')
        facts.create(a, a.target, this, threads, log)
    }

    /**
     * Reloads the systemd daemon.
     */
    def reloadSystemd() {
        shell privileged: true, "systemctl daemon-reload" call()
    }

    /**
     * Stop the specified service.
     */
    def stopSystemdService(String service) {
        stopSystemdService([service])
    }

    /**
     * Stop the specified services.
     */
    def stopSystemdService(List services) {
        log.info 'Stopping {}.', services
        services.each {
            shell privileged: true, "if systemctl list-unit-files --type=service|egrep '$it.*\\s+enabled'; then systemctl stop $it; fi" call()
        }
    }

    /**
     * Start the specified service.
     */
    def startSystemdService(String service) {
        startSystemdService([service])
    }

    /**
     * Start the specified services.
     */
    def startSystemdService(List services) {
        log.info 'Starting {}.', services
        services.each {
            shell privileged: true, "systemctl start $it && systemctl status" call()
        }
    }

    /**
     * Enable the specified service.
     */
    def enableSystemdService(String service) {
        enableSystemdService([service])
    }

    /**
     * Enable the specified services.
     */
    def enableSystemdService(List services) {
        log.info 'Enabling {}.', services
        services.each {
            shell privileged: true, "systemctl enable $it" call()
        }
    }

    /**
     * Start and enable the specified service.
     */
    def startEnableSystemdService(String service) {
        startEnableSystemdService([service])
    }

    /**
     * Start and enable the specified services.
     */
    def startEnableSystemdService(List services) {
        log.info 'Starting and enabling {}.', services
        services.each {
            shell privileged: true, "systemctl start $it && systemctl status $it && systemctl enable $it" call()
        }
    }

    /**
     * Restart and enable the specified service.
     */
    def restartEnableSystemdService(String service) {
        restartEnableSystemdService([service])
    }

    /**
     * Restart and enable the specified services.
     */
    def restartEnableSystemdService(List services) {
        log.info 'Restarting and enabling {}.', services
        services.each {
            shell privileged: true, "systemctl restart $it && systemctl status $it && systemctl enable $it" call()
        }
    }

    /**
     * Restart the specified services.
     */
    def restartSystemdService(List services) {
        log.info 'Restarting {}.', services
        services.each {
            shell privileged: true, "systemctl restart $it && systemctl status $it" call()
        }
    }

    /**
     * Checks if the specified apt packages with a specific version are
     * installed.
     */
    boolean checkAptPackagesVersion(List packages) {
        assertThat "package=null", packages[0], hasKey('package')
        assertThat "version=null", packages[0], hasKey('version')
        def failed = packages.find { Map m ->
            !checkAptPackage(m)
        }
        return failed == null
    }

    /**
     * Checks if the apt packages are installed. Per default checks the
     * packages from the profile property {@code packages}.
     */
    boolean checkAptPackages(List packages=packages) {
        checkAptPackages(packages: packages)
    }

    /**
     * Checks if the apt packages are installed. Per default checks the
     * packages from the profile property {@code packages}.
     */
    boolean checkAptPackages(Map args) {
        List packages = args.packages
        def found = packages.findAll {
            def a = new HashMap(args)
            a.package = it
            checkAptPackage a
        }
        return found.size() == packages.size()
    }

    /**
     * Checks if the apt package is installed.
     * <ul>
     * <li>package: the package name.
     * <li>version: optional, the package version.
     * </ul>
     */
    boolean checkAptPackage(Map args) {
        log.info "Check installed packages {}.", args
        assertThat "args.package=null", args, hasKey('package')
        def a = new HashMap(args)
        a.timeout = args.timeout ? args.timeout : timeoutShort
        a.exitCodes = [0, 1] as int[]
        a.vars = args <<
                [nameInstalled: grepAptPackageNameInstalled] <<
                [versionInstalled: getGrepAptPackageVersionInstalled(args.version)]
        a.st = '''
set -e
export LANG=en_US.UTF-8
s=$(dpkg -s "<vars.package>")
echo $s | grep '<vars.nameInstalled>' 1>/dev/null
i_check=$?
<if(vars.version)>
echo $s | grep '<vars.versionInstalled>' 1>/dev/null
v_check=$?
<else>
v_check=0
<endif>
! (( $i_check || $v_check ))
'''
        def ret = shell a call()
        return ret.exitValue == 0
    }

    /**
     * Installs the specified packages via apt-get. Per default installs the
     * packages from the profile property {@code packages}.
     */
    void installAptPackages(def packages=packages, def timeout=timeoutLong) {
        installAptPackages packages: packages, timeout: timeout
    }

    /**
     * Installs the specified packages via apt-get. Per default installs the
     * packages from the profile property {@code packages}.
     */
    void installAptPackages(Map args) {
        log.info "Installing packages {}.", args
        def a = new HashMap(args)
        a.timeout = a.timeout ? a.timeout : timeoutLong
        a.privileged = true
        a.command = """
apt-get update && apt-get -y install ${args.packages.join(' ')}
"""
        shell a with { //
            sudoEnv "DEBIAN_FRONTEND=noninteractive" } call()
    }

    /**
     * Installs the specified backports packages via apt-get. Per default
     * installs the packages from the profile property {@code packages}.
     */
    void installAptBackportsPackages(def packages=packages, def timeout=timeoutLong) {
        installAptBackportsPackages packages: packages, timeout: timeout
    }

    /**
     * Installs the specified backports packages via apt-get. Per default
     * installs the packages from the profile property {@code packages}.
     */
    void installAptBackportsPackages(Map args) {
        log.info "Installing packages {}.", args
        def a = new HashMap(args)
        a.timeout = a.timeout ? a.timeout : timeoutLong
        a.privileged = true
        a.command = """
apt-get update && apt-get -y -t ${distributionName}-backports install ${args.packages.join(' ')}
"""
        shell a with { //
            sudoEnv "DEBIAN_FRONTEND=noninteractive" } call()
    }

    /**
     * Adds the apt packages repository.
     *
     * @param args
     * <ul>
     * <li>key: repository key;
     * <li>url: repository URL;
     * <li>name: distribution name;
     * <li>comp: repository component;
     * <li>file: repository list file;
     * </ul>
     *
     * @see #getAptPackagesRepositoryKey()
     * @see #getAptPackagesRepository()
     * @see #getAptPackagesRepositoryComponent()
     * @see #getAptPackagesRepositoryListFile()
     */
    def addAptPackagesRepository(Map args=[:]) {
        args.key = args.key ? args.key : aptPackagesRepositoryKey
        args.url = args.url ? args.url : aptPackagesRepositoryUrl
        args.name = args.name ? args.name : distributionName
        args.comp = args.comp ? args.comp : aptPackagesRepositoryComponent
        args.file = args.file ? args.file : aptPackagesRepositoryListFile
        shell """
curl -fsSL ${args.key} | sudo apt-key add -
sudo bash -c 'echo "deb ${args.url} ${args.name} ${args.comp}" > ${args.file}'
""" call()
    }

    /**
     * Adds the apt backports repository.
     *
     * @param args
     * <ul>
     * <li>url: repository URL;
     * <li>name: distribution name;
     * <li>comp: repository component;
     * <li>file: repository list file;
     * </ul>
     *
     * @see #getAptBackportsRepository()
     * @see #getAptBackportsRepositoryComponent()
     * @see #getAptBackportsRepositoryListFile()
     */
    def addAptBackportsRepository(Map args=[:]) {
        args.url = args.url ? args.url : aptBackportsRepositoryUrl
        args.name = args.name ? args.name : "$distributionName-backports"
        args.comp = args.comp ? args.comp : aptBackportsRepositoryComponent
        args.file = args.file ? args.file : aptBackportsRepositoryListFile
        shell """
sudo bash -c 'echo "deb ${args.url} ${args.name} ${args.comp}" > ${args.file}'
""" call()
    }

    /**
     * Checks if the yum package is installed.
     * <ul>
     * <li>package: the package name.
     * <li>version: optional, the package version.
     * </ul>
     */
    boolean checkYumPackage(Map args) {
        log.info "Check installed packages {}.", args
        assertThat "args.package=null", args, hasKey('package')
        def a = new HashMap(args)
        a.timeout = args.timeout ? args.timeout : timeoutShort
        a.exitCodes = [0, 1] as int[]
        a.vars = args
        a.st = '''
set -e
export LANG=en_US.UTF-8
s=$(yum list installed "<vars.package>")
i_check=$?
<if(vars.version)>
echo "$s" | grep '<vars.version>' 1>/dev/null
v_check=$?
<else>
v_check=0
<endif>
! (( $i_check || $v_check ))
'''
        def ret = shell a call()
        return ret.exitValue == 0
    }

    /**
     * Copies the resource by temporarily saving it locally.
     * <pre>
     * copyResource src: src, dest: dest
     * </pre>
     */
    def copyResource(Map args) {
        URL src = args.src.toURL()
        log.info 'Upload {} to {}', args.src, args.dest
        File file = File.createTempFile 'robobee', getBaseName(args.dest.toString())
        try {
            IOUtils.copy src.openStream(), new FileOutputStream(file)
            assertThat "resource>0 for $args", file.size(), greaterThan(0l)
            Map a = new HashMap(args)
            a.src = file
            copy a call()
        } finally {
            file.delete()
        }
    }

    /**
     * Copies the resource by temporarily saving it locally.
     * <pre>
     * copyString str: string, dest: dest
     * </pre>
     */
    def copyString(Map args) {
        String str = args.str.toString()
        log.info 'Upload string to {}', args.dest
        File file = File.createTempFile 'robobee', getBaseName(args.dest.toString())
        try {
            FileUtils.write file, str, charset
            assertThat "string>0 for $args", file.size(), greaterThan(0l)
            Map a = new HashMap(args)
            a.src = file
            copy a call()
        } finally {
            file.delete()
        }
    }

    /**
     * Uploads the TLS certificates.
     * <ul>
     * <li>tls: the TLS;
     * <li>dest: the destination directory;
     * <li>name: the name of the TLS;
     * </ul>
     */
    def uploadTlsCerts(Map args) {
        def tls = args.tls
        def dest = args.dest ? args.dest : certsDir
        def name = args.name
        if (tls) {
            [
                [
                    name: "${name}.ca",
                    src: tls.ca,
                    dest: "$dest/$tls.caName",
                    privileged: true
                ],
                [
                    name: "${name}.cert",
                    src: tls.cert,
                    dest: "$dest/$tls.certName",
                    privileged: true
                ],
                [
                    name: "${name}.key",
                    src: tls.key,
                    dest: "$dest/$tls.keyName",
                    privileged: true
                ],
            ].each {
                if (it.src) {
                    log.debug 'Upload {} TLS', it.name
                    copyResource it
                }
            }
        }
    }

    /**
     * Returns the packages repository apt-key, for
     * example {@code "https://packages.icinga.com/icinga.key"}
     *
     * <ul>
     * <li>profile property {@code apt_packages_repository_key}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getAptPackagesRepositoryKey() {
        properties.getProperty "apt_packages_repository_key", defaultProperties
    }

    /**
     * Returns the packages repository base URL, for
     * example {@code "https://packages.icinga.com/debian"}
     *
     * <ul>
     * <li>profile property {@code apt_packages_pepository_url}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getAptPackagesRepositoryUrl() {
        properties.getProperty "apt_packages_pepository_url", defaultProperties
    }

    /**
     * Returns the packages repository, for
     * example {@code "/etc/apt/sources.list.d/icinga.list"}
     *
     * <ul>
     * <li>profile property {@code apt_packages_repository_list_file}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getAptPackagesRepositoryListFile() {
        getFileProperty "apt_packages_repository_list_file", base, defaultProperties
    }

    /**
     * Returns the packages repository component, for
     * example {@code "main"}
     *
     * <ul>
     * <li>profile property {@code apt_packages_repository_component}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getAptPackagesRepositoryComponent() {
        properties.getProperty "apt_packages_repository_component", defaultProperties
    }

    /**
     * Returns the backports repository base URL, for
     * example {@code "http://deb.debian.org/debian"}
     *
     * <ul>
     * <li>profile property {@code apt_backports_repository_url}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getAptBackportsRepositoryUrl() {
        properties.getProperty "apt_backports_repository_url", defaultProperties
    }

    /**
     * Returns the backports repository list file, for
     * example {@code "/etc/apt/sources.list.d/backports.list"}
     *
     * <ul>
     * <li>profile property {@code apt_backports_repository_list_file}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getAptBackportsRepositoryListFile() {
        getFileProperty "apt_backports_repository_list_file", base, defaultProperties
    }

    /**
     * Returns the backports repository component, for
     * example {@code "main"}
     *
     * <ul>
     * <li>profile property {@code apt_backports_repository_component}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getAptBackportsRepositoryComponent() {
        properties.getProperty "apt_backports_repository_component", defaultProperties
    }

    /**
     * Finds and creates the script.
     */
    HostServiceScript createScript(String name, HostService service, SystemInfo system=target.system) {
        def scriptService = findScriptService(name, system)
        assertThat "service=null for name=$name, system=$system", scriptService, notNullValue()
        createScript scriptService, service
    }

    /**
     * Finds and creates the script.
     */
    HostServiceScript createScript(String name, HostService service, String systemName) {
        def scriptService = findScriptService(name, systemName)
        createScript scriptService, service
    }

    /**
     * Finds and creates the script.
     */
    HostServiceScript createScript(HostServiceScriptService scriptService, HostService service) {
        scriptService.create(scriptsRepository, service, target, threads, scriptEnv)
    }

    /**
     * Finds the script.
     */
    public <T extends HostServiceScriptService> T findScriptService(String name, SystemInfo system=target.system) {
        scriptsRepository.getAvailableScriptService(new AbstractScriptInfo(name, system) { })
    }

    /**
     * Finds the script.
     */
    public <T extends HostServiceScriptService> T findScriptService(String name, String systemName) {
        String scriptName = "$name/${systemName}"
        scriptsRepository.getAvailableScriptService scriptName
    }

    /**
     * Finds the registered service with the specified name and group.
     */
    public List findService(String name, String group) {
        def services = scriptsRepository.getServices name
        services.findAll { HostService s ->
            s.hasProperty('group') && (s.group == group)
        }
    }

    /**
     * Finds available service with the specified name.
     */
    public <T extends HostServiceService> T findAvailableService(String name) {
        scriptsRepository.getAvailableService name
    }

    /**
     * Returns the system name, for
     * example {@code "debian"}
     *
     * <ul>
     * <li>profile property {@code system_name}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getSystemName() {
        properties.getProperty "system_name", defaultProperties
    }

    /**
     * Returns the distribution name, for
     * example {@code "jessie"}
     *
     * <ul>
     * <li>profile property {@code distribution_name}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDistributionName() {
        properties.getProperty "distribution_name", defaultProperties
    }

    /**
     * Returns the distribution name, for
     * example {@code "8"}
     *
     * <ul>
     * <li>profile property {@code system_version}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getSystemVersion() {
        properties.getProperty "system_version", defaultProperties
    }

    /**
     * Returns the repository string, for
     * example {@code "deb http://archive.ubuntu.com/ubuntu <distributionName> <repository>"}
     *
     * <ul>
     * <li>profile property {@code repository_string}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getRepositoryString() {
        properties.getProperty "repository_string", defaultProperties
    }

    /**
     * Returns the needed packages of the service.
     *
     * <ul>
     * <li>profile property {@code packages}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getPackages() {
        properties.getListProperty "packages", defaultProperties
    }

    /**
     * Returns the character set.
     *
     * <ul>
     * <li>profile property {@code charset}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    Charset getCharset() {
        properties.getCharsetProperty "charset", defaultProperties
    }

    /**
     * Returns the configuration file of the service.
     *
     * <ul>
     * <li>profile property {@code config_file}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getConfigFile() {
        properties.getFileProperty "config_file", configDir, defaultProperties
    }

    URI getArchive() {
        properties.getURIProperty 'archive', defaultProperties
    }

    String getArchiveHash() {
        properties.getProperty 'archive_hash', defaultProperties
    }

    String getArchiveSig() {
        properties.getProperty 'archive_sig', defaultProperties
    }

    String getArchiveServer() {
        properties.getProperty 'archive_server', defaultProperties
    }

    String getArchiveKey() {
        properties.getProperty 'archive_key', defaultProperties
    }

    Boolean getArchiveIgnoreKey() {
        properties.getBooleanProperty 'archive_ignore_key', defaultProperties
    }

    File getBinDir() {
        properties.getFileProperty "bin_dir", base, defaultProperties
    }

    File getOptDir() {
        properties.getFileProperty "opt_dir", base, defaultProperties
    }

    /**
     * Returns the configuration directory of the service.
     *
     * <ul>
     * <li>profile property {@code config_dir}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getConfigDir() {
        def dir = properties.getFileProperty "config_dir", base, defaultProperties
    }

    /**
     * Returns the sys-config directory.
     *
     * <ul>
     * <li>profile property {@code sys_config_dir}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getSysConfigDir() {
        def dir = properties.getFileProperty "sys_config_dir", base, defaultProperties
    }

    /**
     * Returns the configuration directory of the certificates for the service.
     *
     * <ul>
     * <li>profile property {@code certs_dir}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getCertsDir() {
        properties.getFileProperty "certs_dir", base, defaultProperties
    }

    /**
     * Creates and returns a temporary file.
     */
    File createTmpFile(Map args=[:]) {
        def file
        if (createTmpFileCallback) {
            file = createTmpFileCallback(args)
        } else {
            def a = new HashMap(args)
            a.outString = true
            a.command = """
file=\$(mktemp)
chmod o-rw \$file
echo \$file
"""
            def ret = shell a call()
            file = new File(ret.out[0..-2])
        }
        file
    }

    /**
     * Creates and returns a temporary directory.
     */
    File createTmpDir(Map args=[:]) {
        def file
        if (createTmpFileCallback) {
            file = createTmpFileCallback(args)
            file.delete()
            file.mkdirs()
        } else {
            def a = new HashMap(args)
            a.outString = true
            a.command = """
file=\$(mktemp -d)
chmod o-rwx \$file
echo \$file
"""
            def ret = shell a call()
            file = new File(ret.out[0..-2])
        }
        file
    }

    /**
     * Deletes the temporary file.
     */
    void deleteTmpFile(File file) {
        deleteTmpFile file: file
    }

    /**
     * Deletes the temporary file.
     */
    void deleteTmpFile(Map args) {
        File file = args.file
        if (createTmpFileCallback) {
            file.isFile() ? file.delete() : file.deleteDir()
        } else {
            def a = new HashMap(args)
            a.command = "rm -rf ${file.absolutePath}"
            shell a call()
        }
    }

    Duration getTimeoutShort() {
        properties.getDurationProperty('command_timeout_short', defaultProperties)
    }

    Duration getTimeoutMiddle() {
        properties.getDurationProperty('command_timeout_middle', defaultProperties)
    }

    Duration getTimeoutLong() {
        properties.getDurationProperty('command_timeout_long', defaultProperties)
    }

    Duration getTimeoutVeryLong() {
        properties.getDurationProperty('command_timeout_very_long', defaultProperties)
    }

    String getGrepAptPackageNameInstalled() {
        properties.getProperty 'grep_apt_package_name_installed', defaultProperties
    }

    String getGrepAptPackageVersionInstalled(String version) {
        def s = properties.getProperty 'grep_apt_package_version_installed', defaultProperties
        new ST(s).add('version', version).render()
    }

    public void putState(String name, Object state) {
        scriptsRepository.putState(name, state)
    }

    public <T> T getState(String name) {
        return (T) scriptsRepository.getState(name)
    }

    /**
     * Returns the file property.
     *
     * @param key
     * the key of the property.
     * @param parent
     * the {@link File} parent directory of the file.
     * @param defaults
     * the {@link ContextProperties} default properties.
     * @param useAbsolute
     * if true, if the file property is absolute then the file property
     * is returned, otherwise the file property will always be under the
     * specified parent directory.
     */
    public File getFileProperty(String key,
            File parent=base,
            ContextProperties defaults=defaultProperties,
            boolean useAbsolute=true) {
        File file
        if (useAbsolute) {
            file = properties.getFileProperty key, base, defaults
            if (!file.absolute) {
                file = properties.getFileProperty key, parent, defaults
            }
        } else {
            file = properties.getFileProperty key, parent, defaults
        }
        return file
    }

    private setupArgs(Map args, String name='') {
        Map a = new HashMap(args)
        a = replaceMapValues env, a, "env"
        if (!args.containsKey('parent')) {
            a.parent = this
        }
        if (!args.containsKey('target')) {
            a.target = target
        }
        if (!a.containsKey('chdir')) {
            a.chdir = chdir
        }
        if (!a.containsKey('sudoChdir')) {
            a.sudoChdir = sudoChdir
        }
        if (!a.containsKey('pwd')) {
            a.pwd = pwd
        }
        if (!a.containsKey('sudoEnv')) {
            a.sudoEnv = sudoEnv
        } else {
            a.sudoEnv.putAll sudoEnv
        }
        if (createTmpFileCallback) {
            args.cmdName = name
            a.tmpFile = createTmpFileCallback(args)
        }
        def v = args.timeout
        if (v) {
            if (!(v instanceof java.time.Duration)) {
                v = DurationFormat.create().parse v.toString()
                a.timeout = v
            }
        }
        a.filesLocal = filesLocal
        return a
    }

    private replaceMapValues(Map replace, Map args, String key) {
        def map = new HashMap(replace)
        map.putAll(args[key] != null ? args[key] : [:])
        args[key] = map
        return args
    }
}
