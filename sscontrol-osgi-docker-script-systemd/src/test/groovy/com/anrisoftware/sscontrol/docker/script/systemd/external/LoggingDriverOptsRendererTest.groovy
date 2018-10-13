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
