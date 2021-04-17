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

import com.anrisoftware.sscontrol.types.app.external.AppException;

/**
 * Thrown if there were errors in the manifest.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@SuppressWarnings("serial")
public class ManifestErrorsException extends AppException {

    public ManifestErrorsException(String name, String output, String error, String manifest) {
        super("Error in manifest");
        addContextValue("name", name);
        addContextValue("output", output);
        addContextValue("error", error);
        addContextValue("manifest", manifest);
    }
}
