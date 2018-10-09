package com.anrisoftware.sscontrol.services.internal.host

import javax.inject.Inject

import com.anrisoftware.sscontrol.types.host.external.HostService
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties
import com.anrisoftware.sscontrol.types.host.external.HostServiceService
import com.anrisoftware.sscontrol.types.ssh.external.SshHost
import com.google.inject.assistedinject.Assisted

import groovy.transform.ToString

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@ToString
class HostnameStub implements HostService {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    interface HostnameStubFactory {

        HostnameStub create(Map<String, Object> args)
    }

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    @ToString
    static class HostnameStubServiceImpl implements HostServiceService {

        @Inject
        HostnameStubFactory serviceFactory

        @Override
        String getName() {
        }

        @Override
        HostService create(Map<String, Object> args) {
            serviceFactory.create(args)
        }
    }

    String name

    List<SshHost> targets

    @Inject
    HostnameStub(@Assisted Map<String, Object> args) {
        this.targets = args.targets
    }

    void set(String name) {
        this.name = name
    }

    @Override
    SshHost getTarget() {
        targets[0]
    }

    @Override
    List<SshHost> getTargets() {
        targets
    }

    @Override
    HostServiceProperties getServiceProperties() {
    }
}
