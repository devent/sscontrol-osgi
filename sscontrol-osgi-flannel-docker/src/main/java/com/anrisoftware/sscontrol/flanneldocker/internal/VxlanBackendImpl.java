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
package com.anrisoftware.sscontrol.flanneldocker.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.flanneldocker.external.Backend;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Vxlan</i> backend.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class VxlanBackendImpl implements Backend {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface VxlanBackendImplFactory extends BackendFactory {

    }

    @AssistedInject
    VxlanBackendImpl() {
        this(new HashMap<String, Object>());
    }

    @AssistedInject
    VxlanBackendImpl(@Assisted Map<String, Object> args) {
        parseArgs(args);
    }

    @Override
    public String getType() {
        return "vxlan";
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("type", getType()).toString();
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("address");
        if (v != null) {
        }
    }

}
