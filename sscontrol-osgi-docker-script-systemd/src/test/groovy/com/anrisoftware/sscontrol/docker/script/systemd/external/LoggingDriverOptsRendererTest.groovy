/*-
 * #%L
 * sscontrol-osgi - docker-script-systemd
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
package com.anrisoftware.sscontrol.docker.script.systemd.external

import org.junit.jupiter.api.Test

/**
 * @see LoggingDriverOptsRenderer
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class LoggingDriverOptsRendererTest {

    @Test
    void "render camel case"() {
        def renderer = new LoggingDriverOptsRenderer()
        [
            [formatString: "camelCaseToDash", input: "maxFile", expected: "max-file"],
            [formatString: "camelCaseToDash", input: "maxSize", expected: "max-size"],
        ].eachWithIndex { it, int k ->
            def s = renderer.toString it.input, it.formatString, null
            assert s == it.expected
        }
    }
}
