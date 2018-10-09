package com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_8

import java.nio.charset.Charset

import javax.inject.Inject

import org.apache.commons.io.FilenameUtils
import org.stringtemplate.v4.ST
import org.stringtemplate.v4.STGroup

import com.anrisoftware.sscontrol.utils.st.base64renderer.external.StringBase64Renderer
import com.anrisoftware.sscontrol.utils.st.base64renderer.external.UriBase64Renderer
import com.anrisoftware.sscontrol.utils.st.base64renderer.external.UrlBase64Renderer

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractSTTemplateParser extends AbstractTemplateParser {

    @Inject
    StringBase64Renderer stringBase64Renderer

    @Inject
    UriBase64Renderer uriBase64Renderer

    @Inject
    UrlBase64Renderer urlBase64Renderer

    def loadTemplate(File parentDirectory, String fileName, Map<String, Object> args, Charset encoding) {
        def st = loadGroup(parentDirectory, fileName)
        st.registerRenderer stringBase64Renderer.attributeType, stringBase64Renderer
        st.registerRenderer uriBase64Renderer.attributeType, uriBase64Renderer
        st.registerRenderer urlBase64Renderer.attributeType, urlBase64Renderer
        def name = FilenameUtils.getBaseName fileName
        return st.getInstanceOf(name)
    }

    String parseTemplate(def template, File parentDirectory, String fileName, Map<String, Object> args, Charset encoding) {
        ST s = template
        s.add 'vars', args.vars
        s.add 'parent', args.parent
        return s.render()
    }

    abstract STGroup loadGroup(File parentDirectory, String fileName)
}
