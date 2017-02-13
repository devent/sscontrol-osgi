/*
 * Copyright 2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.app.main.internal

import static org.ops4j.pax.exam.CoreOptions.*

import org.junit.Test
import org.junit.runner.RunWith
import org.ops4j.pax.exam.Option
import org.ops4j.pax.exam.junit.PaxExam
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy
import org.ops4j.pax.exam.spi.reactors.PerMethod

import com.anrisoftware.sscontrol.hostname.external.HostnameService

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
class HostnameTest extends AbstractTestPax {

    @Test
    void "load hostname service"() {
        def ref = bc.getServiceReference HostnameService
        assert ref != null
    }

    Option[] createConfig(Option[] options) {
        super.createConfig options
        options << mavenBundle("com.anrisoftware.sscontrol", "sscontrol-osgi-hostname").versionAsInProject()
    }
}
