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

import com.anrisoftware.sscontrol.collectd.script.debian.internal.debian_9.Collectd_Debian_9_Module
import com.anrisoftware.sscontrol.docker.script.debian.internal.dockerce_18_debian_9.Dockerce_18_Debian_9_Module
import com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_9.etcd_3_2.EtcdDebianModule
import com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian_9.Fail2ban_Debian_9_Module
import com.anrisoftware.sscontrol.haproxy.script.debian.internal.debian_9.HAProxy_Debian_9_Module
import com.anrisoftware.sscontrol.k8smaster.script.debian.internal.k8smaster_1_13.debian_9.K8sMasterDebianModule
import com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_13.debian_9.K8sNodeDebianModule
import com.anrisoftware.sscontrol.nfs.script.debian.internal.debian_9.Nfs_Debian_9_Module
import com.anrisoftware.sscontrol.registry.docker.service.internal.DockerRegistryModule
import com.anrisoftware.sscontrol.repo.git.script.debian.internal.debian_9.GitRepoDebianModule
import com.anrisoftware.sscontrol.sshd.script.debian.internal.debian_9.SshdDebianModule
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtilsModule

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class DebianModules {

    /**
     * Returns the needed modules.
     */
    static List getModules() {
        [
            new SshdDebianModule(),
            new Collectd_Debian_9_Module(),
            new Fail2ban_Debian_9_Module(),
            new Dockerce_18_Debian_9_Module(),
            new EtcdDebianModule(),
            new Nfs_Debian_9_Module(),
            new K8sMasterDebianModule(),
            new K8sNodeDebianModule(),
            new GitRepoDebianModule(),
            new DockerRegistryModule(),
            new DebianUtilsModule(),
            new HAProxy_Debian_9_Module(),
        ]
    }
}
