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
package com.anrisoftware.sscontrol.fail2ban.script.fail2ban_0_x.external

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.lang3.StringUtils

import com.anrisoftware.globalpom.initfileparser.external.DefaultInitFileAttributes
import com.anrisoftware.globalpom.initfileparser.external.DefaultInitFileAttributesFactory
import com.anrisoftware.globalpom.initfileparser.external.InitFileParserFactory
import com.anrisoftware.globalpom.initfileparser.external.Section
import com.anrisoftware.globalpom.initfileparser.external.SectionFactory
import com.anrisoftware.globalpom.initfileparser.external.SectionFormatterFactory
import com.anrisoftware.sscontrol.fail2ban.service.external.Fail2ban
import com.anrisoftware.sscontrol.fail2ban.service.external.Jail
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * Configures the <i>jail.local</i> file.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Jail_0_x extends ScriptBase {

    @Inject
    InitFileParserFactory initFileParserFactory

    @Inject
    DefaultInitFileAttributesFactory initFileAttributesFactory

    @Inject
    SectionFormatterFactory sectionFormatterFactory

    @Inject
    SectionFactory sectionFactory

    def configureJail() {
        Fail2ban service = service
        withLocalTempFile "jail.local", { tmp ->
            setupDefaultJail service.defaultJail
            def builder = configureJail service.defaultJail, tmp
            write tmp, builder.toString(), charset
            service.jails.each {
                setupDefaults it
                builder = configureJail it, tmp
                write tmp, builder.toString(), charset
            }
            copy privileged: true, src: tmp, dest: jailLocalConfigFile call()
        }
    }

    /**
     * Setups the default jail.
     */
    def setupDefaultJail(Jail jail) {
        def banaction = properties.getProperty "banaction", defaultProperties
        jail.banAction(banaction)
        def action = properties.getProperty "action", defaultProperties
        jail.banning app: " ", action: action
    }

    def configureJail(Jail jail, File jailLocalConfigFile) {
        def attributes = initFileAttributesFactory.create()
        attributes.stringQuoteEnabled = false
        def parser = initFileParserFactory.create(jailLocalConfigFile, attributes)()
        def sections = parser.inject([]) { acc, val -> acc << val }
        def section = sections.find { Section section -> section.name == jail.service }
        sections = setupsSection(section, jail, sections)
        return formatSection(attributes, sections)
    }

    /**
     * Setups the jail.
     */
    def setupDefaults(Jail jail) {
    }

    /**
     * Creates or updates the section based on the jail.
     */
    List setupsSection(Section section, Jail jail, List sections) {
        if (section == null) {
            section = sectionFactory.create(jail.service, new Properties())
            sections << section
        }
        section.with {
            if (jail.enabled) {
                properties.setProperty "enabled", jail.enabled.toString()
            }
            if (jail.notify) {
                properties.setProperty "destemail", jail.notify
            }
            if (jail.port) {
                properties.setProperty "port", jail.port.toString()
            }
            if (jail.ignoreAddresses && jail.ignoreAddresses.size() > 0) {
                properties.setProperty "ignoreip", jail.ignoreAddresses.join(" ")
            }
            if (jail.banAction) {
                properties.setProperty "banaction", jail.banAction
            }
            if (jail.banning.retries) {
                properties.setProperty "maxretry", Integer.toString(jail.banning.retries)
            }
            if (jail.banning.time) {
                properties.setProperty "bantime", Long.toString(jail.banning.time.getStandardSeconds())
            }
            if (jail.banning.backend) {
                properties.setProperty "backend", jail.banning.backend.toString()
            }
            if (jail.banning.action) {
                properties.setProperty "action", jail.banning.action
            }
            if (jail.banning.app) {
                properties.setProperty "app", jail.banning.app
            }
        }
        return sections
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

    /**
     * Returns contains the filter name for the service.
     */
    boolean haveFilter(String name) {
        name = StringUtils.replaceChars(name, "-", "_")
        properties.haveProperty "filter_${name}"
    }

    /**
     * Returns the filter name for the service.
     *
     * @param name
     *            the service name.
     *
     * @return the filter name.
     *
     * @see #getFirewallProperties()
     */
    String filterFor(String name) {
        name = StringUtils.replaceChars(name, "-", "_")
        properties.getProperty "filter_${name}", defaultProperties
    }

    /**
     * Returns contains the ports name for the service.
     *
     * @param name
     *            the service name.
     *
     * @return {@code true} that the profile property was set.
     *
     * @see #getFirewallProperties()
     */
    boolean havePorts(String name) {
        name = StringUtils.replaceChars(name, "-", "_")
        properties.haveProperty "ports_${name}"
    }

    /**
     * Returns the ports for the service.
     *
     * @param name
     *            the service name.
     *
     * @return the ports.
     *
     * @see #getFirewallProperties()
     */
    String portsFor(String name) {
        name = StringUtils.replaceChars(name, "-", "_")
        properties.getListProperty("ports_${name}", defaultProperties).join(",")
    }

    /**
     * Returns contains the logging path for the service.
     *
     * @param name
     *            the service name.
     *
     * @return {@code true} that the profile property was set.
     *
     * @see #getFirewallProperties()
     */
    boolean haveLogpath(String name) {
        name = StringUtils.replaceChars(name, "-", "_")
        properties.haveProperty "logpath_${name}"
    }

    /**
     * Returns the logging path for the service.
     *
     * @param name
     *            the service name.
     *
     * @return the logging path.
     *
     * @see #getFirewallProperties()
     */
    String logpathFor(String name) {
        name = StringUtils.replaceChars(name, "-", "_")
        properties.getProperty "logpath_${name}", defaultProperties
    }

    /**
     * Returns the {@code jail.local} file path.
     */
    File getJailLocalConfigFile() {
        properties.getFileProperty "jail_local_config_file", configDir, defaultProperties
    }

    /**
     * Returns the default action.
     */
    String getAction() {
        properties.getProperty "action", defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
