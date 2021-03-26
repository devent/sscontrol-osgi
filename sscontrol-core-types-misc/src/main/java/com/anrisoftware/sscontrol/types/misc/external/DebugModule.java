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
package com.anrisoftware.sscontrol.types.misc.external;

import java.util.Map;

/**
 * Debug logging module.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface DebugModule {

    /**
     * Returns a new module with the new name.
     */
    DebugModule rename(String name);

    /**
     * Returns the name of the module.
     */
    String getName();

    /**
     * Returns the logging level.
     */
    int getLevel();

    /**
     * Returns the properties.
     */
    Map<String, Object> getProperties();

    /**
     * Returns a new module with the put property value.
     */
    DebugModule putProperty(String property, Object value);

}
