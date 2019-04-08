/**
 * Copyright © 2016 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.muellerpublicde

import com.anrisoftware.sscontrol.k8s.backup.client.internal.BackupClientModule
import com.anrisoftware.sscontrol.k8s.backup.script.linux.internal.script_1_13.BackupLinuxModule
import com.anrisoftware.sscontrol.k8s.fromhelm.script.linux.internal.script_1_13.FromHelmLinuxModule
import com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_13.FileTemplateModule
import com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_13.FromRepositoryLinuxModule
import com.anrisoftware.sscontrol.k8s.restore.script.linux.internal.script_1_13.RestoreLinuxModule
import com.anrisoftware.sscontrol.k8scluster.script.linux.internal.k8scluster_1_13.K8sClusterLinuxModule
import com.anrisoftware.sscontrol.k8scluster.script.linux.internal.k8scluster_1_13.K8sClusterLinuxServiceModule
import com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_13.KubectlLinuxModule
import com.anrisoftware.sscontrol.registry.docker.script.linux.internal.linux.DockerRegistryLinuxModule
import com.anrisoftware.sscontrol.shell.linux.internal.Shell_Linux_Module
import com.anrisoftware.sscontrol.utils.ufw.linux.external.UfwUtilsModule

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class LinuxModules {

    /**
     * Returns the needed modules.
     */
    static List getModules() {
        [
            new KubectlLinuxModule(),
            new K8sClusterLinuxModule(),
            new K8sClusterLinuxServiceModule(),
            new BackupLinuxModule(),
            new RestoreLinuxModule(),
            new BackupClientModule(),
            new FromRepositoryLinuxModule(),
            new FromHelmLinuxModule(),
            new FileTemplateModule(),
            new DockerRegistryLinuxModule(),
            new Shell_Linux_Module(),
            new UfwUtilsModule(),
        ]
    }
}
