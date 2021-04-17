/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.k8scluster.service.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8scluster.service.external.Cluster;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Cluster.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class ClusterImpl implements Cluster {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface ClusterImplFactory {

        Cluster create(Map<String, Object> args);

        Cluster create(Cluster cluster, Map<String, Object> args);
    }

    private String name;

    private final ClusterImplLogger log;

    @AssistedInject
    ClusterImpl(ClusterImplLogger log, @Assisted Cluster cluster,
            @Assisted Map<String, Object> args) {
        this(log, copyArgs(cluster, args));
    }

    private static Map<String, Object> copyArgs(Cluster cluster,
            Map<String, Object> args) {
        Map<String, Object> a = new HashMap<>(args);
        if (!a.containsKey("name")) {
            a.put("name", cluster.getName());
        }
        return a;
    }

    @AssistedInject
    ClusterImpl(ClusterImplLogger log, @Assisted Map<String, Object> args) {
        this.log = log;
        parseArgs(args);
    }

    public void setName(String name) {
        this.name = name;
        log.nameSet(this, name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("name");
        if (v != null) {
            setName(v.toString());
        }
    }

}
