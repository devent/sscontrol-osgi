/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.k8s.fromrepository.service.external;

import java.io.File;

import com.anrisoftware.sscontrol.types.app.external.AppException;

/**
 * Thrown if the specified template was not found.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@SuppressWarnings("serial")
public class TemplateNotFoundException extends AppException {

    public TemplateNotFoundException(File parentDirectory, String fileName) {
        super("Template not found");
        addContextValue("parent-directory", parentDirectory);
        addContextValue("file-name", fileName);
    }
}
