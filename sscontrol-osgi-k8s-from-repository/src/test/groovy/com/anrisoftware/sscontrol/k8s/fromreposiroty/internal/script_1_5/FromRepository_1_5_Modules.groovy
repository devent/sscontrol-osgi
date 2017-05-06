package com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.script_1_5

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.globalpom.core.textmatch.tokentemplate.TokensTemplateModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.groovy.script.external.GroovyScriptModule
import com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.service.FromRepositoryModule
import com.anrisoftware.sscontrol.k8sbase.base.internal.K8sModule
import com.anrisoftware.sscontrol.k8scluster.internal.K8sClusterModule
import com.anrisoftware.sscontrol.k8scluster.linux.internal.k8scluster_1_5.K8sCluster_1_5_Linux_Module
import com.anrisoftware.sscontrol.k8scluster.linux.internal.k8scluster_1_5.K8sCluster_1_5_Linux_Service
import com.anrisoftware.sscontrol.k8scluster.upstream.external.K8sCluster_1_5_Upstream_Module
import com.anrisoftware.sscontrol.repo.git.linux.internal.debian_8.GitRepo_Debian_8_Module
import com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.shell.internal.copy.CopyModule
import com.anrisoftware.sscontrol.shell.internal.facts.FactsModule
import com.anrisoftware.sscontrol.shell.internal.fetch.FetchModule
import com.anrisoftware.sscontrol.shell.internal.replace.ReplaceModule
import com.anrisoftware.sscontrol.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.shell.internal.ssh.CmdImplModule
import com.anrisoftware.sscontrol.shell.internal.ssh.ShellCmdModule
import com.anrisoftware.sscontrol.shell.internal.ssh.SshShellModule
import com.anrisoftware.sscontrol.shell.internal.st.StModule
import com.anrisoftware.sscontrol.shell.internal.template.TemplateModule
import com.anrisoftware.sscontrol.shell.internal.templateres.TemplateResModule
import com.anrisoftware.sscontrol.ssh.internal.SshModule
import com.anrisoftware.sscontrol.ssh.internal.SshPreModule
import com.anrisoftware.sscontrol.tls.internal.TlsModule
import com.anrisoftware.sscontrol.types.host.external.HostServiceScriptService
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
import com.anrisoftware.sscontrol.utils.systemmappings.internal.SystemNameMappingsModule
import com.google.inject.AbstractModule

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class FromRepository_1_5_Modules {

    /**
     * Returns the needed modules.
     */
    static List getAdditionalModules() {
        [
            new SshModule(),
            new SshPreModule(),
            new K8sModule(),
            new K8sClusterModule(),
            new K8sCluster_1_5_Upstream_Module(),
            new K8sCluster_1_5_Linux_Module(),
            new FromRepositoryModule(),
            new FromRepository_1_5_Module(),
            new GitRepoModule(),
            new GitRepo_Debian_8_Module(),
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
            new AbstractModule() {

                @Override
                protected void configure() {
                    bind HostServiceScriptService.class to K8sCluster_1_5_Linux_Service.class
                }
            }
        ]
    }
}
