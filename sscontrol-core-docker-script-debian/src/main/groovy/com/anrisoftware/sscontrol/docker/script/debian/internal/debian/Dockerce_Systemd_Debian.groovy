/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.docker.script.debian.internal.debian

import org.joda.time.Duration
import org.stringtemplate.v4.ST

import com.anrisoftware.sscontrol.docker.service.external.Docker
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils

import groovy.util.logging.Slf4j

/**
 * Configures the Docker CE service for Debian.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Dockerce_Systemd_Debian extends ScriptBase {

    abstract ScriptBase getDockerSystemd()

    abstract DebianUtils getDebian()

    def setupDefaults() {
        log.info "Setup defaults for {}.", service
        Docker service = service
        if (service.cgroups.size() == 0) {
            service.cgroups.addAll(defaultCgroups)
        }
    }

    def installKernel() {
        if (check("uname -a | grep '$kernelFullVersion'")) {
            return
        }
        debian.addBackportsRepository()
        debian.installBackportsPackages([
            "$kernelPackage=$kernelVersion"
        ])
    }

    def updateGrub() {
        log.info "Update Grub for cgroups."
        def b = [
            cgroupsCmdLine,
            swapAccountCmdLine
        ]
        replace privileged: true, dest: grubConfigFile with {
            line "s/(?m)^GRUB_CMDLINE_LINUX=\".*\"/GRUB_CMDLINE_LINUX=\"${b.join(' ')}\"/"
            it
        }()
        shell privileged: true, "update-grub" call()
    }

    String getCgroupsCmdLine() {
        Docker service = service
        if (service.cgroups.size() == 0) {
            return ''
        } else {
            return new ST('cgroup_enable=<cgroups;separator=",">').add('cgroups', service.cgroups).render()
        }
    }

    String getSwapAccountCmdLine() {
        enableSwapaccount ? 'swapaccount=1' : ''
    }

    List getDefaultCgroups() {
        properties.getListProperty 'default_cgroups', defaultProperties
    }

    File getGrubConfigFile() {
        properties.getFileProperty 'grub_config_file', base, defaultProperties
    }

    Duration getAptgetTimeout() {
        properties.getDurationProperty 'apt_get_timeout', defaultProperties
    }

    boolean getUpgradeKernel() {
        properties.getBooleanProperty 'upgrade_kernel', defaultProperties
    }

    String getKernelPackage() {
        properties.getProperty 'kernel_package', defaultProperties
    }

    String getKernelVersion() {
        properties.getProperty 'kernel_version', defaultProperties
    }

    String getKernelFullVersion() {
        properties.getProperty 'kernel_full_version', defaultProperties
    }

    boolean getEnableSwapaccount() {
        properties.getBooleanProperty 'enable_swapaccount', defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
