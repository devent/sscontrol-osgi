package com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.script_1_5

import static org.hamcrest.Matchers.*

import java.nio.charset.Charset

import org.apache.commons.io.FilenameUtils
import org.stringtemplate.v4.ST
import org.stringtemplate.v4.STGroupDir

/**
 * Parses the file via a ST4 template engine.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
class StDirTemplateParser extends AbstractTemplateParser {

    public static String TEMPLATE_NAME = 'st'

    def loadTemplate(File parentDirectory, String fileName, Map<String, Object> args, Charset encoding) {
        def st = new STGroupDir(parentDirectory.absolutePath)
        def name = FilenameUtils.getBaseName fileName
        return st.getInstanceOf(name)
    }

    String parseTemplate(def template, File parentDirectory, String fileName, Map<String, Object> args, Charset encoding) {
        ST s = template
        s.add 'vars', args.vars
        s.add 'parent', args.parent
        return s.render()
    }

    @Override
    String getTemplateName() {
        TEMPLATE_NAME
    }
}
