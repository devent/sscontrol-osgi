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
package com.anrisoftware.sscontrol.shell.external.utils

import javax.inject.Inject

import com.anrisoftware.globalpom.exec.external.command.CommandLineFactory
import com.anrisoftware.globalpom.exec.external.core.CommandExecFactory
import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.globalpom.threads.properties.external.PropertiesThreadsFactory
import com.google.inject.Injector

/**
 * Executes a specific command.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class CmdExecHelper {

	/**
	 * Executes ssh to create a master socket.
	 */
	static File createSshSocket(Map args, Injector injector) {
		def idFile = args.idFile
		def host = args.host
		def socketFile = File.createTempFile("robobee", ".socket")
		def idArg = idFile != null ? "-i $idFile" : ""
		socketFile.delete()
		injector.getInstance CmdExecHelperFactory create() cmd("ssh",
				idArg,
				"-o \"ControlMaster=yes\"",
				"-o \"ControlPath=$socketFile\"",
				"-o \"ControlPersist=60\"", host, "sleep 60")
		return socketFile
	}

	@Inject
	CommandLineFactory commandLineFactory

	@Inject
	CommandExecFactory commandExecFactory

	Threads threads

	@Inject
	void createThreads(PropertiesThreadsFactory threadsFactory,
			ThreadsTestPropertiesProvider threadsProperties) {
		def threads = threadsFactory.create()
		threads.setProperties threadsProperties.get()
		threads.setName("cmd_exec_helper")
		this.threads = threads
	}

	/**
	 * Runs the specified command.
	 *
	 * @param args
	 * the command and plus additional arguments.
	 */
	def cmd(String... args) {
		def line = commandLineFactory.create(args[0])
		(1..(args.size()-1)).each { int i ->
			line.add args[i]
		}
		def exec = commandExecFactory.create()
		exec.setThreads threads
		def task = exec.exec line
		task.get()
	}
}
