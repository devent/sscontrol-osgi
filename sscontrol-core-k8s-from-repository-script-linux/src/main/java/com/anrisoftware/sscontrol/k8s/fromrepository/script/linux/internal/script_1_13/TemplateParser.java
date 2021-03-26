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
package com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_13;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Parses the files via a template engine.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface TemplateParser {

    String parseFile(File parentDirectory, String fileName,
            Map<String, Object> args, Charset encoding);

    /**
     * Returns the template name.
     */
    String getTemplateName();

    /**
     * Checks if the file with the specified name is a Kubernetes manifest.
     */
    boolean isKubeFile(String fileName);

    /**
     * Returns the target file name.
     */
    String getFilename(String fileName);

}
