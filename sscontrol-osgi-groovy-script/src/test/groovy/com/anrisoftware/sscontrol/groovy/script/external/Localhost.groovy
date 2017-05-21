/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.groovy.script.external

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
