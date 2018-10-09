package com.anrisoftware.sscontrol.docker.service.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.docker.service.external.Mirror;
import com.anrisoftware.sscontrol.docker.service.external.Registry;
import com.anrisoftware.sscontrol.docker.service.internal.MirrorImpl.MirrorImplFactory;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Docker registry.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class RegistryImpl implements Registry {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface RegistryImplFactory {

        Registry create();

        Registry create(Map<String, Object> args);

    }

    private final RegistryImplLogger log;

    private final List<Mirror> mirrorHosts;

    private final MirrorImplFactory mirrorFactory;

    @AssistedInject
    RegistryImpl(RegistryImplLogger log, MirrorImplFactory mirrorFactory) {
        this(log, mirrorFactory, new HashMap<String, Object>());
    }

    @AssistedInject
    RegistryImpl(RegistryImplLogger log, MirrorImplFactory mirrorFactory,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.mirrorFactory = mirrorFactory;
        this.mirrorHosts = new ArrayList<>();
        parseArgs(args);
    }

    public void addMirror(Map<String, Object> args) {
        Mirror mirror = mirrorFactory.create(args);
        mirrorHosts.add(mirror);
        log.mirrorAdded(this, mirror);
    }

    @Override
    public List<Mirror> getMirrorHosts() {
        return mirrorHosts;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("mirror");
        if (v != null) {
            Map<String, Object> a = new HashMap<>(args);
            a.put("host", v);
            addMirror(a);
        }
    }

}
