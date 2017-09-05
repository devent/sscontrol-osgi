package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_7

import java.util.concurrent.ExecutorService

import com.anrisoftware.sscontrol.types.host.external.HostService
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.host.external.TargetHost

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class ScriptMock implements HostServiceScript {

    @Override
    Object run() {
    }

    @Override
    String getSystemName() {
    }

    @Override
    String getSystemVersion() {
    }

    @Override
    Object getLog() {
    }

    @Override
    HostServiceProperties getProperties() {
    }

    @Override
    <T extends ExecutorService> T getThreads() {
    }

    @Override
    Properties getDefaultProperties() {
    }

    @Override
    HostService getService() {
    }

    @Override
    HostServices getScriptsRepository() {
    }

    @Override
    TargetHost getTarget() {
    }
}
