package com.anrisoftware.sscontrol.app.main.internal

import static org.ops4j.pax.exam.CoreOptions.*

import javax.inject.Inject

import org.junit.Test
import org.junit.runner.RunWith
import org.ops4j.pax.exam.Option
import org.ops4j.pax.exam.junit.PaxExam
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy
import org.ops4j.pax.exam.spi.reactors.PerMethod

import com.anrisoftware.sscontrol.types.host.external.HostServicesService
import com.anrisoftware.sscontrol.types.parser.external.ParserService

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
class ParserServiceTest extends AbstractTestPax {

    static final URI emptyScript = ParserServiceTest.class.getResource('Empty.groovy').toURI()

    @Inject
    HostServicesService hostServicesService

    @Test
    void "load parser service"() {
        def refs = bc.getServiceReferences ParserService, null
        assert refs.size() == 1
        refs.each { ref ->
            ParserService s = bc.getService ref
            bc.ungetService ref
            assert s != null
            doTest s
        }
    }

    void doTest(ParserService service) {
        def parent = emptyScript.toString()
        int index = parent.lastIndexOf '/'
        parent = parent.substring(0, index + 1)
        def roots = [new URI(parent)] as URI[]
        def name = 'Empty.groovy'
        def variables = [:]
        def hostServices = hostServicesService.create()
        def parser = service.create(roots, name, variables, hostServices)
        parser.parse()
        assert parser != null
    }

    List<Option> createConfig(List<Option> options) {
        super.createConfig options
        options << mavenBundle("com.anrisoftware.sscontrol", "sscontrol-osgi-groovy-parser").versionAsInProject()
    }
}
