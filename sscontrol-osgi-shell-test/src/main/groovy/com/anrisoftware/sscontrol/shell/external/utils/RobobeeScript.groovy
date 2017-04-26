package com.anrisoftware.sscontrol.shell.external.utils

import java.util.concurrent.Callable

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.sscontrol.types.external.host.HostServices
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
