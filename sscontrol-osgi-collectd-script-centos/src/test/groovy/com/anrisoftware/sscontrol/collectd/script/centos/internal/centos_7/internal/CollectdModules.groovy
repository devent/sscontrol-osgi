package com.anrisoftware.sscontrol.collectd.script.centos.internal.centos_7.internal

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.globalpom.core.textmatch.tokentemplate.TokensTemplateModule
import com.anrisoftware.sscontrol.collectd.script.centos.internal.centos_7.Collectd_Centos_7_Module
import com.anrisoftware.sscontrol.collectd.service.internal.CollectdModule
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
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.ssh.service.internal.SshModule
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
import com.anrisoftware.sscontrol.utils.centos.external.CentosUtilsModule
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtilsModule
import com.anrisoftware.sscontrol.utils.systemmappings.internal.SystemNameMappingsModule

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class CollectdModules {

    /**
     * Returns the needed modules.
     */
    static List getAdditionalModules() {
	[
	    new SshModule(),
	    new CollectdModule(),
	    new Collectd_Centos_7_Module(),
	    new CentosUtilsModule(),
	    new SystemdUtilsModule(),
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
	    new TokensTemplateModule(),
	    new ResourcesModule(),
	    new SystemNameMappingsModule(),
	    new StModule(),
	]
    }
}
