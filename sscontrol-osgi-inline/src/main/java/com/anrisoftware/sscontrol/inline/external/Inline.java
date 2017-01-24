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
package com.anrisoftware.sscontrol.inline.external;

import java.util.List;

import com.anrisoftware.sscontrol.types.external.HostService;

/**
 * <i>Inline</i> service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Inline extends HostService {

    /**
     * Returns the scripts to run.
     * <p>
     *
     * <pre>
     * // Add script.
     * script << ""
     * // Multi-line.
     * script """
     * """
     * // Dollar multi-line.
     * script $/
     * /$
     * </pre>
     */
    List<String> getScripts();

}
