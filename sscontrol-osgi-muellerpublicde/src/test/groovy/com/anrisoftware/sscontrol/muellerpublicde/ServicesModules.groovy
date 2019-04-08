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
package com.anrisoftware.sscontrol.muellerpublicde

import com.anrisoftware.sscontrol.collectd.service.internal.CollectdModule
import com.anrisoftware.sscontrol.docker.service.internal.DockerModule
import com.anrisoftware.sscontrol.etcd.service.internal.BindingModule
import com.anrisoftware.sscontrol.etcd.service.internal.EtcdModule
import com.anrisoftware.sscontrol.fail2ban.service.internal.Fail2banModule
import com.anrisoftware.sscontrol.haproxy.service.internal.HAProxyModule
import com.anrisoftware.sscontrol.k8s.backup.client.internal.BackupClientModule
import com.anrisoftware.sscontrol.k8s.backup.service.internal.BackupModule
import com.anrisoftware.sscontrol.k8s.fromhelm.service.internal.FromHelmModule
import com.anrisoftware.sscontrol.k8s.fromrepository.service.internal.FromRepositoryModule
import com.anrisoftware.sscontrol.k8s.restore.service.internal.RestoreModule
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sModule
import com.anrisoftware.sscontrol.k8scluster.service.internal.K8sClusterModule
import com.anrisoftware.sscontrol.k8smaster.service.internal.K8sMasterModule
import com.anrisoftware.sscontrol.k8snode.service.internal.K8sNodeModule
import com.anrisoftware.sscontrol.nfs.service.internal.NfsModule
import com.anrisoftware.sscontrol.registry.docker.service.internal.DockerRegistryModule
import com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoModule
import com.anrisoftware.sscontrol.shell.internal.ShellModule
import com.anrisoftware.sscontrol.ssh.service.internal.SshModule
import com.anrisoftware.sscontrol.sshd.service.internal.SshdModule

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class ServicesModules {

    /**
     * Returns the needed modules.
     */
    static List getModules() {
        [
            new SshModule(),
            new SshdModule(),
            new CollectdModule(),
            new Fail2banModule(),
            new DockerModule(),
            new EtcdModule(),
            new NfsModule(),
            new BindingModule(),
            new K8sModule(),
            new K8sClusterModule(),
            new K8sMasterModule(),
            new K8sNodeModule(),
            new BackupModule(),
            new RestoreModule(),
            new BackupClientModule(),
            new FromRepositoryModule(),
            new FromHelmModule(),
            new GitRepoModule(),
            new DockerRegistryModule(),
            new ShellModule(),
            new HAProxyModule(),
        ]
    }
}
