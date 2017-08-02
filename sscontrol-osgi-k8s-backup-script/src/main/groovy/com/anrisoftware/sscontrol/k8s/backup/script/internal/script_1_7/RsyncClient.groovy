package com.anrisoftware.sscontrol.k8s.backup.script.internal.script_1_7

import javax.inject.Inject

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8s.backup.service.external.Backup
import com.google.inject.assistedinject.Assisted

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class RsyncClient {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    interface RsyncClientFactory {

        RsyncClient create(ScriptBase script, Backup service)
    }

    @Inject
    @Assisted
    ScriptBase script

    @Inject
    @Assisted
    Backup service

    void start() {
    }
}
