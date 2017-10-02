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
package com.anrisoftware.sscontrol.k8s.glusterfsheketi.script.debian.internal.k8s_1_8.debian_9

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.globalpom.core.textmatch.tokentemplate.TokensTemplateModule
import com.anrisoftware.sscontrol.command.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.command.shell.internal.copy.CopyModule
import com.anrisoftware.sscontrol.command.shell.internal.facts.FactsModule
import com.anrisoftware.sscontrol.command.shell.internal.fetch.FetchModule
import com.anrisoftware.sscontrol.command.shell.internal.replace.ReplaceModule
import com.anrisoftware.sscontrol.command.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.command.shell.internal.ssh.CmdImplModule
import com.anrisoftware.sscontrol.command.shell.internal.ssh.ShellCmdModule
import com.anrisoftware.sscontrol.command.shell.internal.ssh.SshShellModule
import com.anrisoftware.sscontrol.command.shell.internal.st.StModule
import com.anrisoftware.sscontrol.command.shell.internal.template.TemplateModule
import com.anrisoftware.sscontrol.command.shell.internal.templateres.TemplateResModule
import com.anrisoftware.sscontrol.command.shell.linux.openssh.internal.find.FindModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_7.FileTemplateModule
import com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_7.FromRepositoryLinuxModule
import com.anrisoftware.sscontrol.k8s.fromrepository.service.internal.FromRepositoryModule
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal.GlusterfsHeketiModule
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sModule
import com.anrisoftware.sscontrol.k8scluster.script.linux.internal.k8scluster_1_8.K8sClusterLinuxModule
import com.anrisoftware.sscontrol.k8scluster.script.linux.internal.k8scluster_1_8.K8sClusterLinuxServiceModule
import com.anrisoftware.sscontrol.k8scluster.service.internal.K8sClusterModule
import com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_8.KubectlLinuxModule
import com.anrisoftware.sscontrol.registry.docker.script.linux.internal.linux.DockerRegistryLinuxModule
import com.anrisoftware.sscontrol.registry.docker.service.internal.DockerRegistryModule
import com.anrisoftware.sscontrol.repo.git.script.debian.internal.debian_9.GitRepoDebianModule
import com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.shell.internal.ShellModule
import com.anrisoftware.sscontrol.shell.linux.internal.Shell_Linux_Module
import com.anrisoftware.sscontrol.ssh.service.internal.SshModule
import com.anrisoftware.sscontrol.tls.internal.TlsModule
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
import com.anrisoftware.sscontrol.types.ssh.external.TargetsListModule
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtilsModule
import com.anrisoftware.sscontrol.utils.systemmappings.internal.SystemNameMappingsModule
import com.anrisoftware.sscontrol.utils.ufw.linux.external.UfwUtilsModule

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class GlusterfsHeketiTestModules {

    /**
     * Returns the needed modules.
     */
    static List getAdditionalModules() {
        [
            new SshModule(),
            new K8sModule(),
            new K8sClusterModule(),
            new K8sClusterLinuxModule(),
            new KubectlLinuxModule(),
            new FromRepositoryModule(),
            new FromRepositoryLinuxModule(),
            new FileTemplateModule(),
            new GitRepoModule(),
            new GitRepoDebianModule(),
            new DockerRegistryModule(),
            new DockerRegistryLinuxModule(),
            new GlusterfsHeketiModule(),
            new GlusterfsHeketiDebianModule(),
            new ShellModule(),
            new Shell_Linux_Module(),
            new DebianUtilsModule(),
            new UfwUtilsModule(),
            new DebugLoggingModule(),
            new TypesModule(),
            new StringsModule(),
            new HostServicesModule(),
            new ShellCmdModule(),
            new SshShellModule(),
            new CmdImplModule(),
            new CmdModule(),
            new ScpModule(),
            new CopyModule(),
            new FetchModule(),
            new ReplaceModule(),
            new FactsModule(),
            new TemplateModule(),
            new TemplateResModule(),
            new StModule(),
            new TokensTemplateModule(),
            new ResourcesModule(),
            new TlsModule(),
            new SystemNameMappingsModule(),
            new FindModule(),
            new K8sClusterLinuxServiceModule(),
            new TargetsListModule(),
        ]
    }
}
