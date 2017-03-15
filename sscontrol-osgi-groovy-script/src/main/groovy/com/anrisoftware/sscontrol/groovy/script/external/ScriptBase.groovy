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

import static org.apache.commons.lang3.Validate.*
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import java.nio.charset.Charset
import java.util.concurrent.ExecutorService

import javax.inject.Inject

import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.builder.ToStringBuilder
import org.joda.time.Duration

import com.anrisoftware.sscontrol.copy.external.Copy
import com.anrisoftware.sscontrol.copy.external.Copy.CopyFactory
import com.anrisoftware.sscontrol.facts.external.Facts
import com.anrisoftware.sscontrol.facts.external.Facts.FactsFactory
import com.anrisoftware.sscontrol.fetch.external.Fetch
import com.anrisoftware.sscontrol.fetch.external.Fetch.FetchFactory
import com.anrisoftware.sscontrol.replace.external.Replace
import com.anrisoftware.sscontrol.replace.external.Replace.ReplaceFactory
import com.anrisoftware.sscontrol.shell.external.Shell
import com.anrisoftware.sscontrol.shell.external.Shell.ShellFactory
import com.anrisoftware.sscontrol.template.external.Template
import com.anrisoftware.sscontrol.template.external.Template.TemplateFactory
import com.anrisoftware.sscontrol.types.external.HostService
import com.anrisoftware.sscontrol.types.external.HostServiceProperties
import com.anrisoftware.sscontrol.types.external.HostServiceScript
import com.anrisoftware.sscontrol.types.external.HostServices
import com.anrisoftware.sscontrol.types.external.SshHost
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
    SshHost target

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
        shell.create(a, a.target, this, threads, log)
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
        fetch.create(a, a.target, this, threads, log)
    }

    /**
     * Copy command.
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
        copy.create(a, a.target, this, threads, log)
    }

    /**
     * Replace command.
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
     */
    Template template(Map args) {
        def a = setupArgs(args, 'template')
        template.create(a, a.target, this, threads, log)
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
            shell privileged: true, "if systemctl list-unit-files --type=service|grep $it; then systemctl stop $it; fi" call()
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
     * Installs the specified packages via apt-get. Per default installs the
     * packages from the profile property {@code packages}.
     */
    void installAptPackages(def packages=packages, def timeout=timeoutLong) {
        log.info "Installing packages {}.", packages
        shell privileged: true, timeout: timeoutLong, "apt-get update && apt-get -y install ${packages.join(' ')}" with { //
            sudoEnv "DEBIAN_FRONTEND=noninteractive" } call()
    }

    /**
     * Copies the resource by temporarily saving it locally.
     * <pre>
     * copyResource src: src, dest: dest call()
     * </pre>
     */
    def copyResource(Map args) {
        URL src = args.src.toURL()
        log.info 'Upload {} to {}', args.src, args.dest
        File file = createTmpFile()
        IOUtils.copy src.openStream(), new FileOutputStream(file)
        assertThat "resource>0 for $args", file.size(), greaterThan(0l)
        copy privileged: args.privileged, src: file, dest: args.dest
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
        def dest = args.certsDir ? args.dest : certsDir
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
                    name: '${name}.cert',
                    src: tls.cert,
                    dest: "$dest/$tls.certName",
                    privileged: true
                ],
                [
                    name: '${name}.key',
                    src: tls.key,
                    dest: "$dest/$tls.keyName",
                    privileged: true
                ],
            ].each {
                if (it.src) {
                    copyResource it call()
                }
            }
        }
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
        if (createTmpFileCallback) {
            createTmpFileCallback(args)
        } else {
            File.createTempFile('robobee', null)
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

    private setupArgs(Map args, String name='') {
        Map a = new HashMap(args)
        a = replaceMapValues env, a, "env"
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
