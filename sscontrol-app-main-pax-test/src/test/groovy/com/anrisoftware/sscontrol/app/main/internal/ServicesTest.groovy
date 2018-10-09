package com.anrisoftware.sscontrol.app.main.internal

import static org.ops4j.pax.exam.CoreOptions.*

import org.junit.Test
import org.junit.runner.RunWith
import org.ops4j.pax.exam.Option
import org.ops4j.pax.exam.junit.PaxExam
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy
import org.ops4j.pax.exam.spi.reactors.PerMethod

import com.anrisoftware.sscontrol.hostname.external.HostnameService
import com.anrisoftware.sscontrol.types.host.external.HostServiceService

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
class ServicesTest extends AbstractTestPax {

    @Test
    void "load hostname services"() {
        def refs = bc.getServiceReferences HostServiceService, null
        assert refs.size() == 1
        refs.each { ref ->
            def keys = ref.getPropertyKeys()
            println keys
            keys.each { println ref.getProperty(it) }
            HostnameService s = bc.getService ref
            bc.ungetService ref
            assert s != null
            //s.create([:])
        }
    }

    List<Option> createConfig(List<Option> options) {
        super.createConfig options
        options << mavenBundle("com.anrisoftware.sscontrol", "sscontrol-osgi-hostname").versionAsInProject()
    }
}
