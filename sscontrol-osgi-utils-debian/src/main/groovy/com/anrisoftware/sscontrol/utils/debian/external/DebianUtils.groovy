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
        script.shell a with { //
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
        script.shell a with { //
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
     * @see #getAptBackportsRepository()
     * @see #getAptBackportsRepositoryComponent()
     * @see #getAptBackportsRepositoryListFile()
     */
    def addAptBackportsRepository(Map args=[:]) {
        args.url = args.url ? args.url : aptBackportsRepositoryUrl
        args.name = args.name ? args.name : "$distributionName-backports"
        args.comp = args.comp ? args.comp : aptBackportsRepositoryComponent
        args.file = args.file ? args.file : aptBackportsRepositoryListFile
        script.shell """
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
        def ret = script.shell a call()
        return ret.exitValue == 0
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
    String getAptPackagesRepositoryUrl() {
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
    File getAptPackagesRepositoryListFile() {
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
    String getAptPackagesRepositoryComponent() {
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
    String getAptBackportsRepositoryUrl() {
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
    File getAptBackportsRepositoryListFile() {
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
    String getAptBackportsRepositoryComponent() {
        script.properties.getProperty "apt_backports_repository_component", defaultProperties
    }
}
