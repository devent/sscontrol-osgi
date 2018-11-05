/*-
 * #%L
 * sscontrol-osgi - k8s-from-repository-script-linux
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_12

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