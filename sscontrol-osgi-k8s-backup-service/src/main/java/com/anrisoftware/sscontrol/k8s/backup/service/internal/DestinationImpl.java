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
package com.anrisoftware.sscontrol.k8s.backup.service.internal;

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.core.resources.ToURI;
import com.anrisoftware.sscontrol.k8s.backup.service.external.Destination;
import com.google.inject.assistedinject.Assisted;

/**
 * Backup destination.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class DestinationImpl implements Destination {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface DestinationImplFactory {

        Destination create(Map<String, Object> args);

    }

    private URI dest;

    @Inject
    DestinationImpl(@Assisted Map<String, Object> args) {
        parseArgs(args);
    }

    public void setDest(URI dest) {
        this.dest = dest;
    }

    @Override
    public URI getDest() {
        return dest;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        parseName(args);
    }

    private void parseName(Map<String, Object> args) {
        Object v = args.get("dir");
        if (v != null) {
            setDest(ToURI.toURI(v).convert());
        }
    }

}
