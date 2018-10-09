package com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_8

import org.stringtemplate.v4.STGroup
import org.stringtemplate.v4.STGroupFile

/**
 * Parses the group file via a ST4 template engine.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
class StgFileTemplateParser extends AbstractSTTemplateParser {

    public static String TEMPLATE_NAME = 'stg'

    @Override
    STGroup loadGroup(File parentDirectory, String fileName) {
        new STGroupFile(new File(parentDirectory, fileName).absolutePath)
    }

    @Override
    String getTemplateName() {
        TEMPLATE_NAME
    }
}
