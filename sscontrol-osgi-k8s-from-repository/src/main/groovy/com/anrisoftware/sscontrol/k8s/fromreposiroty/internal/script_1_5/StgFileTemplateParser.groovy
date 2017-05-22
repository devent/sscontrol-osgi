package com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.script_1_5

import java.nio.charset.Charset

import org.apache.commons.io.FilenameUtils
import org.stringtemplate.v4.STGroupFile

/**
 * Parses the group file via a ST4 template engine.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
class StgFileTemplateParser extends AbstractTemplateParser {

    public static String TEMPLATE_NAME = 'stg'

    @Override
    String parseFile(File parentDirectory, String fileName, Map<String, Object> args, Charset encoding) {
        def st = new STGroupFile(new File(parentDirectory, fileName).absolutePath)
        def name = FilenameUtils.getBaseName fileName
        def s = st.getInstanceOf(name)
        s.add 'vars', args.vars
        s.add 'parent', args.parent
        return s.render()
    }

    @Override
    public boolean getNeedCopyRepo() {
        return true;
    }

    @Override
    String getTemplateName() {
        TEMPLATE_NAME
    }
}
