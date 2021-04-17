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
package com.anrisoftware.sscontrol.inline.linux.external

import org.apache.commons.io.FileUtils

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.inline.external.Inline
import com.anrisoftware.sscontrol.types.external.HostServiceScript
import com.anrisoftware.sscontrol.types.external.HostServices

import groovy.util.logging.Slf4j

/**
 * Configures the <i>inline</i> service systems.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Inline_Linux extends ScriptBase {

    def runService() {
        Inline service = service
        service.scripts.each {
            File file = createTmpFile()
            try {
                FileUtils.write file, it, charset
                runScript file
            } finally {
                file.delete()
            }
        }
    }

    HostServices runScript(File file) {
        Binding binding = createBinding services
        GroovyScriptEngine engine = new GroovyScriptEngine([file.parentFile.toURL()] as URL[]);
        engine.run(file.name, binding);
        return binding.service
    }

    Binding createBinding() {
        Binding binding = new Binding();
        binding.setProperty("service", services);
        binding.setProperty("targets", services.getTargets());
        return binding;
    }

    HostServiceScript setupScript(HostServiceScript script) {
        script.setChdir chdir
        script.setPwd pwd
        script.setSudoEnv sudoEnv
        script.setEnv env
        return script
    }

    @Override
    def getLog() {
        log
    }
}
