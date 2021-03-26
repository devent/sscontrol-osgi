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
