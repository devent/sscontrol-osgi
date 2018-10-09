package com.anrisoftware.sscontrol.services.internal.host

import javax.inject.Inject

import org.apache.commons.lang3.StringUtils

import com.anrisoftware.sscontrol.types.misc.external.DebugLogging
import com.anrisoftware.sscontrol.types.host.external.HostService
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties
import com.anrisoftware.sscontrol.types.ssh.external.Ssh
import com.anrisoftware.sscontrol.types.ssh.external.SshHost
import com.anrisoftware.sscontrol.types.ssh.external.TargetServiceService
import com.google.inject.assistedinject.Assisted

import groovy.transform.ToString

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@ToString
class SshStub implements Ssh {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    interface SshStubFactory {

        SshStub create(Map<String, Object> args)
    }

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    @ToString
    static class SshStubServiceImpl implements TargetServiceService {

        @Inject
        SshStubFactory serviceFactory

        @Override
        String getName() {
        }

        @Override
        HostService create(Map<String, Object> args) {
            serviceFactory.create(args)
        }
    }

    String group

    List<SshHost> targets

    List<SshHost> hosts

    @Inject
    SshStub(@Assisted Map<String, Object> args) {
        this.targets = args.targets
        this.hosts = []
    }

    void group(String name) {
        this.group = name
    }

    String getGroup() {
        if (StringUtils.isEmpty(group)) {
            return "default"
        } else {
            return group
        }
    }

    void host(String name) {
        hosts.add([
            getHost: { name }
        ] as SshHost)
    }

    @Override
    String getName() {
        'ssh'
    }

    @Override
    List<SshHost> getHosts() {
        hosts
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

    @Override
    DebugLogging getDebugLogging() {
        return null
    }
}
