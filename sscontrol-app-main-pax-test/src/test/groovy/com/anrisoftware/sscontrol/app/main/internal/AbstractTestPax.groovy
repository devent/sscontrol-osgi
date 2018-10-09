package com.anrisoftware.sscontrol.app.main.internal

import static org.ops4j.pax.exam.CoreOptions.*

import javax.inject.Inject

import org.ops4j.pax.exam.Configuration
import org.ops4j.pax.exam.Option
import org.ops4j.pax.exam.util.PathUtils
import org.osgi.framework.BundleContext

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractTestPax {

    @Inject
    BundleContext bc

    @Configuration
    Option[] config() {
        def options = []
        options.addAll createConfig(options)
        options
    }

    List<Option> createConfig(List<Option> options) {
        options << junitBundles()
        options << systemProperty('logback.configurationFile').value("file:${PathUtils.baseDir}/src/test/resources/logback-test.xml")
        options << mavenBundle("org.apache.felix", "org.apache.felix.scr", "1.6.0")
        options << mavenBundle("org.slf4j", "slf4j-api").versionAsInProject()
        options << mavenBundle("ch.qos.logback", "logback-classic").versionAsInProject()
        options << mavenBundle("ch.qos.logback", "logback-core").versionAsInProject()
        options << mavenBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.aopalliance").versionAsInProject()
        options << mavenBundle("com.google.guava", "guava").versionAsInProject()
        options << mavenBundle("org.codehaus.groovy", "groovy").versionAsInProject()
        options << mavenBundle("com.google.inject", "guice").versionAsInProject()
        options << mavenBundle("com.google.inject.extensions", "guice-assistedinject").versionAsInProject().noStart()
        options << mavenBundle("org.apache.commons", "commons-lang3").versionAsInProject().noStart()
        options << mavenBundle("joda-time", "joda-time").versionAsInProject().noStart()
        options << mavenBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.antlr-runtime").versionAsInProject().noStart()
        options << mavenBundle("com.anrisoftware.globalpom", "globalpom-log").versionAsInProject()
        options << mavenBundle("com.anrisoftware.resources", "resources-api").versionAsInProject()
        options << mavenBundle("com.anrisoftware.resources", "resources-getbundle").versionAsInProject()
        options << mavenBundle("com.anrisoftware.resources", "resources-templates").versionAsInProject()
        options << mavenBundle("com.anrisoftware.resources", "resources-st").versionAsInProject()
        options << mavenBundle("com.anrisoftware.propertiesutils", "propertiesutils-contextproperties").versionAsInProject()
        options << mavenBundle("com.anrisoftware.sscontrol", "sscontrol-osgi-types").versionAsInProject()
        options << mavenBundle("com.anrisoftware.sscontrol", "sscontrol-osgi-services-repository").versionAsInProject()
        options << mavenBundle("com.anrisoftware.sscontrol", "sscontrol-osgi-services-properties").versionAsInProject()
    }
}
