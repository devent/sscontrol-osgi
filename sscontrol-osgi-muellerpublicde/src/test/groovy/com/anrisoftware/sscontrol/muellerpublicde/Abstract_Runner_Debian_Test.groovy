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

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import com.anrisoftware.sscontrol.collectd.script.debian.internal.debian_9.Collectd_Debian_9_Factory
import com.anrisoftware.sscontrol.collectd.service.internal.CollectdImpl.CollectdImplFactory
import com.anrisoftware.sscontrol.docker.script.debian.internal.dockerce_18_debian_9.Dockerce_18_Debian_9_Factory
import com.anrisoftware.sscontrol.docker.service.internal.DockerImpl.DockerImplFactory
import com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_9.etcd_3_2.EtcdDebianFactory
import com.anrisoftware.sscontrol.etcd.service.internal.EtcdImpl.EtcdImplFactory
import com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian_9.Fail2ban_Debian_9_Factory
import com.anrisoftware.sscontrol.fail2ban.service.internal.Fail2banImpl.Fail2banImplFactory
import com.anrisoftware.sscontrol.haproxy.script.debian.internal.debian_9.HAProxy_1_8_Debian_9_Factory
import com.anrisoftware.sscontrol.haproxy.service.internal.HAProxyImplFactory
import com.anrisoftware.sscontrol.k8s.backup.script.linux.internal.script_1_13.BackupLinuxFactory
import com.anrisoftware.sscontrol.k8s.backup.service.internal.BackupImpl.BackupImplFactory
import com.anrisoftware.sscontrol.k8s.fromhelm.script.linux.internal.script_1_13.FromHelmLinuxFactory
import com.anrisoftware.sscontrol.k8s.fromhelm.service.internal.FromHelmImpl.FromHelmImplFactory
import com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_13.FromRepositoryLinuxFactory
import com.anrisoftware.sscontrol.k8s.fromrepository.service.internal.FromRepositoryImpl.FromRepositoryImplFactory
import com.anrisoftware.sscontrol.k8s.restore.script.linux.internal.script_1_13.RestoreLinuxFactory
import com.anrisoftware.sscontrol.k8s.restore.service.internal.RestoreImpl.RestoreImplFactory
import com.anrisoftware.sscontrol.k8scluster.script.linux.internal.k8scluster_1_13.K8sClusterLinuxFactory
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterFactory
import com.anrisoftware.sscontrol.k8smaster.script.debian.internal.k8smaster_1_13.debian_9.K8sMasterDebianFactory
import com.anrisoftware.sscontrol.k8smaster.service.internal.K8sMasterImpl.K8sMasterImplFactory
import com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_13.debian_9.K8sNodeDebianFactory
import com.anrisoftware.sscontrol.k8snode.service.internal.K8sNodeImpl.K8sNodeImplFactory
import com.anrisoftware.sscontrol.nfs.script.debian.internal.debian_9.Nfs_1_3_Debian_9_Factory
import com.anrisoftware.sscontrol.nfs.service.internal.NfsImplFactory
import com.anrisoftware.sscontrol.registry.docker.script.linux.internal.linux.DockerRegistryLinuxFactory
import com.anrisoftware.sscontrol.registry.docker.service.internal.DockerRegistryImpl.DockerRegistryImplFactory
import com.anrisoftware.sscontrol.repo.git.script.debian.internal.debian_9.GitRepoDebianFactory
import com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoImpl.GitRepoImplFactory
import com.anrisoftware.sscontrol.runner.groovy.internal.RunnerModule
import com.anrisoftware.sscontrol.runner.groovy.internal.RunScriptImpl.RunScriptImplFactory
import com.anrisoftware.sscontrol.shell.internal.ShellImpl.ShellImplFactory
import com.anrisoftware.sscontrol.shell.linux.external.Shell_Linux_Factory
import com.anrisoftware.sscontrol.ssh.script.linux.external.Ssh_Linux_Factory
import com.anrisoftware.sscontrol.ssh.script.linux.internal.Ssh_Linux_Module
import com.anrisoftware.sscontrol.ssh.service.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.sshd.script.debian.internal.debian_9.SshdDebianFactory
import com.anrisoftware.sscontrol.sshd.service.internal.SshdImpl.SshdImplFactory
import com.anrisoftware.sscontrol.types.host.external.HostServices

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
abstract class Abstract_Runner_Debian_Test extends Abstract_Runner_Andrea_Test {

    @Inject
    RunScriptImplFactory runnerFactory

    @Inject
    SshImplFactory sshFactory

    @Inject
    Ssh_Linux_Factory sshLinuxFactory

    @Inject
    SshdImplFactory sshdFactory

    @Inject
    SshdDebianFactory sshdDebianFactory

    @Inject
    Fail2banImplFactory fail2banFactory

    @Inject
    Fail2ban_Debian_9_Factory fail2banDebianFactory

    @Inject
    DockerImplFactory dockerFactory

    @Inject
    Dockerce_18_Debian_9_Factory dockerDebianFactory

    @Inject
    EtcdImplFactory etcdFactory

    @Inject
    EtcdDebianFactory etcdDebianFactory

    @Inject
    NfsImplFactory nfsFactory

    @Inject
    Nfs_1_3_Debian_9_Factory nfsDebianFactory

    @Inject
    K8sClusterFactory clusterFactory

    @Inject
    K8sClusterLinuxFactory clusterLinuxFactory

    @Inject
    GitRepoImplFactory gitFactory

    @Inject
    GitRepoDebianFactory gitDebianFactory

    @Inject
    DockerRegistryImplFactory dockerRegistryFactory

    @Inject
    DockerRegistryLinuxFactory dockerRegistryDebianFactory

    @Inject
    K8sMasterImplFactory k8smasterFactory

    @Inject
    K8sMasterDebianFactory k8smasterDebianFactory

    @Inject
    K8sNodeImplFactory k8snodeFactory

    @Inject
    K8sNodeDebianFactory k8snodeDebianFactory

    @Inject
    FromRepositoryImplFactory fromRepositoryFactory

    @Inject
    FromRepositoryLinuxFactory fromRepositoryLinuxFactory

    @Inject
    FromHelmImplFactory fromHelmFactory

    @Inject
    FromHelmLinuxFactory fromHelmLinuxFactory

    @Inject
    ShellImplFactory shellFactory

    @Inject
    Shell_Linux_Factory shellLinuxFactory

    @Inject
    BackupImplFactory backupFactory

    @Inject
    BackupLinuxFactory backupLinuxFactory

    @Inject
    RestoreImplFactory restoreFactory

    @Inject
    RestoreLinuxFactory restoreLinuxFactory

    @Inject
    CollectdImplFactory collectdFactory

    @Inject
    Collectd_Debian_9_Factory collectdDebianFactory

    @Inject
    HAProxyImplFactory haproxyFactory

    @Inject
    HAProxy_1_8_Debian_9_Factory haproxyDebianFactory

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailableScriptService 'ssh/linux/0', sshLinuxFactory
        services.putAvailableService 'sshd', sshdFactory
        services.putAvailableScriptService 'sshd/debian/9', sshdDebianFactory
        services.putAvailableService 'fail2ban', fail2banFactory
        services.putAvailableScriptService 'fail2ban/debian/9', fail2banDebianFactory
        services.putAvailableService 'collectd', collectdFactory
        services.putAvailableScriptService 'collectd-5.7/debian/9', collectdDebianFactory
        services.putAvailableService 'docker', dockerFactory
        services.putAvailableScriptService 'docker/debian/9', dockerDebianFactory
        services.putAvailableService 'etcd', etcdFactory
        services.putAvailableScriptService 'etcd/debian/9', etcdDebianFactory
        services.putAvailableService 'nfs', nfsFactory
        services.putAvailableScriptService 'nfs-1.3/debian/9', nfsDebianFactory
        services.putAvailableService 'k8s-cluster', clusterFactory
        services.putAvailableScriptService 'k8s/cluster/linux/0', clusterLinuxFactory
        services.putAvailableService 'repo-git', gitFactory
        services.putAvailableScriptService 'repo-git/debian/9', gitDebianFactory
        services.putAvailableService 'registry-docker', dockerRegistryFactory
        services.putAvailableScriptService 'registry-docker/debian/9', dockerRegistryDebianFactory
        services.putAvailableService 'k8s-master', k8smasterFactory
        services.putAvailableScriptService 'k8s-master/debian/9', k8smasterDebianFactory
        services.putAvailableService 'k8s-node', k8snodeFactory
        services.putAvailableScriptService 'k8s-node/debian/9', k8snodeDebianFactory
        services.putAvailableService 'from-repository', fromRepositoryFactory
        services.putAvailableScriptService 'from-repository/linux/0', fromRepositoryLinuxFactory
        services.putAvailableService 'from-helm', fromHelmFactory
        services.putAvailableScriptService 'from-helm/linux/0', fromHelmLinuxFactory
        services.putAvailableService 'shell', shellFactory
        services.putAvailableScriptService 'shell/linux/0', shellLinuxFactory
        services.putAvailableService 'backup', backupFactory
        services.putAvailableScriptService 'backup/linux/0', backupLinuxFactory
        services.putAvailableService 'restore', restoreFactory
        services.putAvailableScriptService 'restore/linux/0', restoreLinuxFactory
        services.putAvailableService 'haproxy', haproxyFactory
        services.putAvailableScriptService 'haproxy-1.8/debian/9', haproxyDebianFactory
        return services
    }

    List getAdditionalModules() {
        def modules = super.additionalModules
        modules << new RunnerModule()
        modules << new Ssh_Linux_Module()
        modules.addAll OtherModules.modules
        modules.addAll ServicesModules.modules
        modules.addAll LinuxModules.modules
        modules.addAll DebianModules.modules
        modules.addAll CentosModules.modules
        modules
    }
}
