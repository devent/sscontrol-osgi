package com.anrisoftware.sscontrol.k8s.glusterfsheketi.internal.script_1_6

import static org.hamcrest.Matchers.*

import java.nio.charset.Charset

import org.apache.commons.io.FilenameUtils
import org.stringtemplate.v4.STGroupDir

import com.anrisoftware.sscontrol.k8s.fromreposiroty.external.TemplateNotFoundException
import com.anrisoftware.sscontrol.k8s.fromreposiroty.external.TemplateParseException

/**
 * Parses the file via a ST4 template engine.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
class StDirTemplateParser extends AbstractTemplateParser {

    public static String TEMPLATE_NAME = 'st'

    @Override
    String parseFile(File parentDirectory, String fileName, Map<String, Object> args, Charset encoding) {
        def st = new STGroupDir(parentDirectory.absolutePath)
        def name = FilenameUtils.getBaseName fileName
        try {
            def s = st.getInstanceOf(name)
            if (!s) {
                throw new TemplateNotFoundException(parentDirectory, fileName, name)
            }
            s.add 'vars', args.vars
            s.add 'parent', args.parent
            return s.render()
        } catch (e) {
            throw new TemplateParseException(e, parentDirectory, fileName, name)
        }
    }

    @Override
    String getTemplateName() {
        TEMPLATE_NAME
    }

    @Override
    boolean getNeedCopyRepo() {
        true
    }
}
