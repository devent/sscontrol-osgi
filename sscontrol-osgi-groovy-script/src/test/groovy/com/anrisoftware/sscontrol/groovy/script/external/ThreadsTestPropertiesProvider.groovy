package com.anrisoftware.sscontrol.groovy.script.external;

import groovy.transform.CompileStatic

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@CompileStatic
class ThreadsTestPropertiesProvider extends AbstractContextPropertiesProvider {

    private static final URL RES = ThreadsTestPropertiesProvider.class.getResource("threads_test.properties");

    ThreadsTestPropertiesProvider() {
        super('com.anrisoftware.globalpom.threads.properties.internal', RES);
    }
}
