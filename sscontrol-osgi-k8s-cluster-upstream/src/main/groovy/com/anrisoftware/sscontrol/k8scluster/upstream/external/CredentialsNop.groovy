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
package com.anrisoftware.sscontrol.k8scluster.upstream.external

import javax.inject.Inject

import com.anrisoftware.sscontrol.k8scluster.external.Credentials
import com.google.inject.assistedinject.Assisted

import groovy.transform.ToString

/**
 * Empty credentials.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@ToString
class CredentialsNop implements Credentials {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    static interface CredentialsNopFactory {

        Credentials create(Map args)
    }

    @Inject
    @Assisted
    private Map args

    @Override
    public String getType() {
        'anon'
    }

    void setProperty(String propertyName, Object newValue) {
        args[propertyName] = newValue
    }

    @Override
    public String getName() {
        args.name
    }

    @Override
    public String getHost() {
        args.host
    }

    @Override
    public Integer getPort() {
        args.port
    }
}
