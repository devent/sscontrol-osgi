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
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
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
