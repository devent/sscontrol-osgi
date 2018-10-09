package com.anrisoftware.sscontrol.shell.external.utils

import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder

/**
 * Module to install the helper to execute a specific command.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class CmdExecHelperModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(CmdExecHelper.class,
				CmdExecHelper.class).build(CmdExecHelperFactory.class))
	}
}
