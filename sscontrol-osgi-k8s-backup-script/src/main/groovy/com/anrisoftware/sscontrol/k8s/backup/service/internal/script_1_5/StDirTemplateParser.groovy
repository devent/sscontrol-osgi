package com.anrisoftware.sscontrol.k8s.backup.service.internal.script_1_5

import static org.hamcrest.Matchers.*

import org.stringtemplate.v4.STGroup
import org.stringtemplate.v4.STGroupDir

/**
 * Parses the file via a ST4 template engine.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
class StDirTemplateParser extends AbstractSTTemplateParser {

    public static String TEMPLATE_NAME = 'st'

    @Override
    STGroup loadGroup(File parentDirectory, String fileName) {
        new STGroupDir(parentDirectory.absolutePath)
    }

    @Override
    String getTemplateName() {
        TEMPLATE_NAME
    }
}
