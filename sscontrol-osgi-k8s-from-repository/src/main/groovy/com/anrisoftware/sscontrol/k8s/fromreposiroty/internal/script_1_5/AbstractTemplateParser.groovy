package com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.script_1_5

import java.nio.charset.Charset

import com.anrisoftware.sscontrol.k8s.fromreposiroty.external.TemplateNotFoundException
import com.anrisoftware.sscontrol.k8s.fromreposiroty.external.TemplateParseException

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractTemplateParser implements TemplateParser {

    @Override
    String parseFile(File parentDirectory, String fileName, Map<String, Object> args, Charset encoding) {
        try {
            def s = loadTemplate parentDirectory, fileName, args, encoding
            if (!s) {
                throw new TemplateNotFoundException(parentDirectory, fileName)
            }
            return parseTemplate(s, parentDirectory, fileName, args, encoding)
        } catch (e) {
            throw new TemplateParseException(e, parentDirectory, fileName)
        }
    }

    def loadTemplate(File parentDirectory, String fileName, Map<String, Object> args, Charset encoding) {
    }

    String parseTemplate(def template, File parentDirectory, String fileName, Map<String, Object> args, Charset encoding) {
    }

    @Override
    boolean isKubeFile(String fileName) {
        String[] split = fileName.split(/\./)
        def name = split[-2]
        def m = (name =~ /(.*)-(\w*)/)
        m.find()
    }

    @Override
    String getFilename(String fileName) {
        String[] split = fileName.split(/\./)
        def name = split[-2]
        def m = (name =~ /(.*)-(\w*)/)
        m.find()
        "${m.group(1)}.${m.group(2)}"
    }
}
