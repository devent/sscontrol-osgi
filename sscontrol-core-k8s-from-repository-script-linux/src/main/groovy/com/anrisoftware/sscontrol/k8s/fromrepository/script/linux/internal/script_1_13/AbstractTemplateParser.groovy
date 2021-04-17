/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_13

import java.nio.charset.Charset

import com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.external.TemplateNotFoundException
import com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.external.TemplateParseException

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
        def match = false
        if (!match) {
            match = (name =~ /(.*)-(\w*)/).find()
        }
        if (!match) {
            match = (name =~ /Dockerfile.*/).find()
        }
        return match
    }

    @Override
    String getFilename(String fileName) {
        String[] split = fileName.split(/\./)
        def name = split[-2]
        def match = false
        def m = (name =~ /(.*)-(\w*)/)
        match = m.find()
        if (match) {
            return "${m.group(1)}.${m.group(2)}"
        }
        m = (name =~ /(Dockerfile)\.?(.*)/)
        match = m.find()
        if (match) {
            if (m.groupCount() == 3) {
                return "${m.group(1)}.${m.group(2)}"
            }
            if (m.groupCount() == 2) {
                return m.group(1)
            }
        }
    }
}
