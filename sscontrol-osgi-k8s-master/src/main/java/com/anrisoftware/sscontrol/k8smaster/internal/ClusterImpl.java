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
package com.anrisoftware.sscontrol.k8smaster.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8smaster.external.Cluster;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * K8s cluster.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ClusterImpl implements Cluster {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ClusterImplFactory {

        Cluster create();

        Cluster create(@Assisted Map<String, Object> args);

    }

    private String serviceRange;

    @AssistedInject
    ClusterImpl() {
        this(new HashMap<String, Object>());
    }

    @AssistedInject
    ClusterImpl(@Assisted Map<String, Object> args) {
        parseArgs(args);
    }

    public void setRange(String range) {
        this.serviceRange = range;
    }

    @Override
    public String getServiceRange() {
        return serviceRange;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("range", getServiceRange()).toString();
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("range");
        if (v != null) {
            this.serviceRange = v.toString();
        }
    }
}
