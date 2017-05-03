package com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.script_1_5

import java.nio.charset.Charset

import org.apache.commons.io.FileUtils
import org.stringtemplate.v4.STGroupString

/**
 * Parses the file via a ST4 template engine.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
class StgFileTemplate implements FileTemplate {

    public static String TEMPLATE_NAME = 'stg'

    @Override
    String parseFile(File file, Map<String, Object> args, Charset encoding) {
        def st = new STGroupString(FileUtils.readFileToString(file, encoding))
        def s = st.getInstanceOf('wordpress_yaml')
        s.add 'vars', args.vars
        s.add 'parent', args.parent
        return s.render()
    }

    @Override
    String getFilename(File file) {
        String[] split = file.name.split(/\./)
        return split[-2]
    }
}
