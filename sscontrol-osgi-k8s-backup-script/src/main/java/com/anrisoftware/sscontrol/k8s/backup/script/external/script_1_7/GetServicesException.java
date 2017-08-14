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
package com.anrisoftware.sscontrol.k8s.backup.script.external.script_1_7;

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase;
import com.anrisoftware.sscontrol.types.app.external.AppException;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class GetServicesException extends AppException {

    public GetServicesException(ScriptBase script, String namespace,
            String name, Exception cause) {
        super("Get services error", cause);
        addContextValue("namespace", namespace);
        addContextValue("name", name);
        addContextValue("script", script);
    }
}
