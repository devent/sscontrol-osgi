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
package com.anrisoftware.sscontrol.utils.debian.external

import static org.apache.commons.io.FilenameUtils.*
import static org.apache.commons.lang3.Validate.*
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 * Debian utilities.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class DebianUtils {

    final HostServiceScript script

    protected DebianUtils(HostServiceScript script) {
        this.script = script
    }

    /**
     * Returns the default {@link Properties} for the service.
     */
    abstract Properties getDefaultProperties()

    /**
     * Returns Debian {@link TemplateResource}.
     */
    abstract TemplateResource getCommandsTemplate()

    /**
     * Checks if the specified apt packages with a specific version are
     * installed.
     */
    boolean checkPackagesVersion(List packages) {
        assertThat "package=null", packages[0], hasKey('package')
        assertThat "version=null", packages[0], hasKey('version')
        def failed = packages.find { Map m ->
            !checkPackage(m)
        }
        return failed == null
    }

    /**
     * Checks if the apt packages are installed. Per default checks the
     * packages from the profile property {@code packages}.
     */
    boolean checkPackages(List packages=packages) {
        checkPackages(packages: packages)
    }

    /**
     * Checks if the apt packages are installed. Per default checks the
     * packages from the profile property {@code packages}.
     */
    boolean checkPackages(Map args) {
        List packages = args.packages
        def found = packages.findAll {
            def a = new HashMap(args)
            a.package = it
            checkPackage a
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
    boolean checkPackage(Map args) {
        log.info "Check installed packages {}.", args
        assertThat "args.package=null", args, hasKey('package')
        def a = new HashMap(args)
        a.timeout = args.timeout ? args.timeout : script.timeoutShort
        a.exitCodes = [0, 1] as int[]
        a.vars = args <<
                [nameInstalled: grepPackageNameInstalled] <<
                [versionInstalled: getGrepPackageVersionInstalled(args.version)]
        a.resource = commandsTemplate
        a.name = 'checkPackage'
        def ret = script.shell a call()
        return ret.exitValue == 0
    }

    /**
     * Installs the specified packages via apt
     * @param packages the List of packages to install. Defaults to the
     * profile property {@code packages}.
     * @param checkInstalled set to true to check if the packages are
     * already installed. Defaults to true.
     * @param timeout the timeout Duration. Defaults to {@code timeoutLong}.
     */
    void installPackages(List packages=script.packages, boolean checkInstalled=true, def timeout=script.timeoutLong) {
        installPackages packages: packages, timeout: timeout, vars: [checkInstalled: checkInstalled]
    }

    /**
     * Installs the specified packages via apt. Per default installs the
     * packages from the profile property {@code packages}.
     */
    void installPackages(Map args) {
        log.info "Installing packages {}.", args
        List packages = args.packages
        def a = new HashMap(args)
        a.vars = args.vars ? new HashMap(args.vars) : [:]
        a.timeout = a.timeout ? a.timeout : script.timeoutLong
        a.privileged = true
        a.resource = commandsTemplate
        a.name = 'installPackage'
        packages.each {
            Map b = new HashMap(a)
            b.vars.package = it
            script.shell b call()
        }
    }

    /**
     * Installs the specified backports packages via apt-get. Per default
     * installs the packages from the profile property {@code packages}.
     */
    void installBackportsPackages(def packages=script.packages, boolean checkInstalled=true, def timeout=script.timeoutLong) {
        def args = [:]
        args.packages = packages
        args.timeout = timeout
        args.vars = [:]
        args.vars.backport = "${script.distributionName}-backports"
        args.vars.checkInstalled = checkInstalled
        installPackages args
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
     * @see #getPackagesRepositoryKey()
     * @see #getPackagesRepository()
     * @see #getPackagesRepositoryComponent()
     * @see #getPackagesRepositoryListFile()
     */
    def addPackagesRepository(Map args=[:]) {
        args.key = args.key ? args.key : packagesRepositoryKey
        args.url = args.url ? args.url : packagesRepositoryUrl
        args.name = args.name ? args.name : script.distributionName
        args.comp = args.comp ? args.comp : packagesRepositoryComponent
        args.file = args.file ? args.file : packagesRepositoryListFile
        script.shell """
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
     * @see #getBackportsRepository()
     * @see #getBackportsRepositoryComponent()
     * @see #getBackportsRepositoryListFile()
     */
    def addBackportsRepository(Map args=[:]) {
        args.url = args.url ? args.url : backportsRepositoryUrl
        args.name = args.name ? args.name : "${script.distributionName}-backports"
        args.comp = args.comp ? args.comp : backportsRepositoryComponent
        args.file = args.file ? args.file : backportsRepositoryListFile
        script.shell """
sudo bash -c 'echo "deb ${args.url} ${args.name} ${args.comp}" > ${args.file}'
""" call()
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
    String getPackagesRepositoryKey() {
        script.properties.getProperty "apt_packages_repository_key", defaultProperties
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
    String getPackagesRepositoryUrl() {
        script.properties.getProperty "apt_packages_pepository_url", defaultProperties
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
    File getPackagesRepositoryListFile() {
        script.getFileProperty "apt_packages_repository_list_file", script.base, defaultProperties
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
    String getPackagesRepositoryComponent() {
        script.properties.getProperty "apt_packages_repository_component", defaultProperties
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
    String getBackportsRepositoryUrl() {
        script.properties.getProperty "apt_backports_repository_url", defaultProperties
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
    File getBackportsRepositoryListFile() {
        script.getFileProperty "apt_backports_repository_list_file", script.base, defaultProperties
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
    String getBackportsRepositoryComponent() {
        script.properties.getProperty "apt_backports_repository_component", defaultProperties
    }
}
