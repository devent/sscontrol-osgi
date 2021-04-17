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

import java.util.concurrent.Callable

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.google.inject.assistedinject.Assisted

/**
 * Runs the script from a GroovyScriptEngine.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class RobobeeScript implements Callable<HostServices> {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    interface RobobeeScriptFactory {

        RobobeeScript create(File file, String script, Map scriptVars, HostServices services)
    }

    @Inject
    @Assisted
    File file

    @Inject
    @Assisted
    String script

    @Inject
    @Assisted
    HostServices services

    @Inject
    @Assisted
    Map scriptVars

    HostServices call() {
        Binding binding = createBinding()
        FileUtils.write file, script
        GroovyScriptEngine engine = new GroovyScriptEngine([file.parentFile.toURL()] as URL[])
        engine.run(file.name, binding)
        return binding.service
    }

    private Binding createBinding() {
        Binding binding = new Binding()
        binding.setProperty("service", services)
        binding.setProperty("targets", services.getTargets())
        scriptVars.each { key, value ->
            binding.setProperty(key, value)
        }
        return binding
    }
}
