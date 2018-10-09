package com.anrisoftware.sscontrol.ssh.service.internal

import com.anrisoftware.sscontrol.types.host.external.SystemInfo
import com.anrisoftware.sscontrol.types.ssh.external.SshHost

import groovy.transform.ToString

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@ToString
class Localhost implements SshHost {

    @Override
    String getHost() {
        'localhost'
    }

    @Override
    String getUser() {
        System.getProperty('user.name')
    }

    @Override
    Integer getPort() {
        22
    }

    @Override
    String getProto() {
        'ssh'
    }

    @Override
    URI getKey() {
    }

    @Override
    String getHostAddress() {
        '127.0.0.1'
    }

    @Override
    SystemInfo getSystem() {
        return null
    }

    @Override
    File getSocket() {
        return null
    }
}
