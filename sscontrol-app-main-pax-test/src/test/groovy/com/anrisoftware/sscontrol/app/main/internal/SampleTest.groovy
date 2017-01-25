package com.anrisoftware.sscontrol.app.main.internal

import static org.ops4j.pax.exam.CoreOptions.*

import javax.inject.Inject

import org.junit.Test
import org.junit.runner.RunWith
import org.ops4j.pax.exam.Configuration
import org.ops4j.pax.exam.Option
import org.ops4j.pax.exam.junit.PaxExam
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy
import org.ops4j.pax.exam.spi.reactors.PerMethod
import org.ops4j.pax.exam.util.PathUtils
import org.osgi.framework.BundleContext

import com.anrisoftware.sscontrol.types.external.HostServiceService

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class SampleTest {

    @Inject
    BundleContext bundleContext

    @Inject
    HostServiceService helloService

    @Configuration
    Option[] config() {
        def options = []
        options << junitBundles()
        options << systemProperty('logback.configurationFile').value("file:${PathUtils.baseDir}/src/test/resources/logback-test.xml")
        options << mavenBundle("org.slf4j", "slf4j-api").versionAsInProject()
        options << mavenBundle("ch.qos.logback", "logback-classic").versionAsInProject()
        options << mavenBundle("ch.qos.logback", "logback-core").versionAsInProject()
        options << mavenBundle("org.codehaus.groovy", "groovy").versionAsInProject()
        options << mavenBundle("com.anrisoftware.globalpom", "globalpom-log").versionAsInProject()
        options << mavenBundle("com.anrisoftware.sscontrol", "sscontrol-app-main").versionAsInProject()
        options << mavenBundle("com.anrisoftware.sscontrol", "sscontrol-osgi-hostname").versionAsInProject()
        options << mavenBundle("com.anrisoftware.sscontrol", "sscontrol-osgi-types").versionAsInProject()
        //options << bundle("reference:file:target/classes")
    }

    @Test
    void getHelloService() {
        helloService != null
    }
}
