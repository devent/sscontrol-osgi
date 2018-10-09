package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_8

import static com.anrisoftware.globalpom.utils.TestUtils.*

import com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_8.KubectlLinuxModule

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class KubectlClientTestModules {

    static List getModules() {
        [
            new KubectlLinuxModule(),
        ]
    }
}
