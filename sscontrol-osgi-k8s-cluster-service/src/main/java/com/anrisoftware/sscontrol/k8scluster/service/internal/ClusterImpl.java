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
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ClusterImpl implements Cluster {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
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
