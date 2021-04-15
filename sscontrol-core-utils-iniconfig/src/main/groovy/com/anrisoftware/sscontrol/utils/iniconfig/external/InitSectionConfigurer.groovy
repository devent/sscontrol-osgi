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
package com.anrisoftware.sscontrol.utils.iniconfig.external

import javax.inject.Inject

import com.anrisoftware.globalpom.initfileparser.external.DefaultInitFileAttributes
import com.anrisoftware.globalpom.initfileparser.external.DefaultInitFileAttributesFactory
import com.anrisoftware.globalpom.initfileparser.external.InitFileParserFactory
import com.anrisoftware.globalpom.initfileparser.external.Section
import com.anrisoftware.globalpom.initfileparser.external.SectionFactory
import com.anrisoftware.globalpom.initfileparser.external.SectionFormatterFactory

/**
 * Loads a INI configuration file and updates the section with properties.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
class InitSectionConfigurer {

    @Inject
    InitFileParserFactory initFileParserFactory

    @Inject
    DefaultInitFileAttributesFactory initFileAttributesFactory

    @Inject
    SectionFormatterFactory sectionFormatterFactory

    @Inject
    SectionFactory sectionFactory

    StringBuilder setupConfig(File file, String name, Map p) {
        def attributes = initFileAttributesFactory.create()
        attributes.stringQuoteEnabled = false
        def parser = initFileParserFactory.create(file, attributes)()
        def sections = parser.inject([]) { acc, val -> acc << val }
        def section = sections.find { Section section -> section.name == name }
        if (section == null) {
            section = sectionFactory.create(name, new Properties())
            sections << section
        }
        p.each { key, value ->
            section.properties.setProperty key, value
        }
        return formatSection(attributes, sections)
    }

    private StringBuilder formatSection(DefaultInitFileAttributes attributes, def sections) {
        def builder = new StringBuilder()
        def formatter = sectionFormatterFactory.create attributes
        sections.each {
            formatter.format it, builder
            builder.append attributes.newLine
        }
        return builder
    }
}
