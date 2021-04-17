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
package com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.external;

import java.io.File;

import com.anrisoftware.sscontrol.types.app.external.AppException;

/**
 * Thrown if the specified template could not be parsed.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@SuppressWarnings("serial")
public class TemplateParseException extends AppException {

    public TemplateParseException(Throwable cause, File parentDirectory,
            String fileName) {
        super("Error parsing template", cause);
        addContextValue("parent-directory", parentDirectory);
        addContextValue("file-name", fileName);
    }
}
