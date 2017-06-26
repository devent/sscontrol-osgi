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
package com.anrisoftware.sscontrol.utils.centos.external

import static org.apache.commons.io.FilenameUtils.*
import static org.apache.commons.lang3.Validate.*
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 * CentOS utilities.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class CentosUtils {

    final HostServiceScript script

    protected CentosUtils(HostServiceScript script) {
        this.script = script
    }

    /**
     * Returns the default {@link Properties} for the service.
     */
    abstract Properties getDefaultProperties()

    /**
     * Checks if the specified yum packages with a specific version are
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
     * Checks if the yum packages are installed. Per default checks the
     * packages from the profile property {@code packages}.
     */
    boolean checkPackages(List packages=script.packages) {
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
     * Checks if the yum package is installed.
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
     * Installs the specified packages via yum. Per default installs the
     * packages from the profile property {@code packages}.
     */
    void installPackages(def packages=script.packages, def timeout=script.timeoutLong) {
        installPackages packages: packages, timeout: timeout
    }

    /**
     * Installs the specified packages via yum. Per default installs the
     * packages from the profile property {@code packages}.
     */
    void installPackages(Map args) {
        log.info "Installing packages {}.", args
        def a = new HashMap(args)
        a.timeout = a.timeout ? a.timeout : script.timeoutLong
        a.privileged = true
        a.command = """
yum install -y ${args.packages.join(' ')}
"""
        script.shell a call()
    }
}
