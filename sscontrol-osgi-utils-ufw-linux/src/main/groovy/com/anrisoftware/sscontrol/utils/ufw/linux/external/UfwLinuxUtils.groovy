package com.anrisoftware.sscontrol.utils.ufw.linux.external

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript
import com.google.inject.assistedinject.Assisted

/**
 * Ufw Linux utilities.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class UfwLinuxUtils extends UfwUtils {

    TemplateResource ufwCommandsTemplate

    @Inject
    UfwLinuxUtils(
    @Assisted HostServiceScript script) {
        super(script)
    }

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create('UfwLinuxUtilsTemplates')
        this.ufwCommandsTemplate = templates.getResource('ufw_commands')
    }
}
