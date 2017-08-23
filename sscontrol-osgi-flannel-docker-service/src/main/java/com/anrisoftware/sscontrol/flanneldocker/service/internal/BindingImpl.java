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
package com.anrisoftware.sscontrol.flanneldocker.service.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.flanneldocker.service.external.Binding;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Binding</i>.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class BindingImpl implements Binding {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface BindingImplFactory {

        Binding create();

        Binding create(@Assisted Map<String, Object> args);

    }

    private String name;

    @AssistedInject
    BindingImpl() {
        this(new HashMap<String, Object>());
    }

    @AssistedInject
    BindingImpl(@Assisted Map<String, Object> args) {
        parseArgs(args);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName()).toString();
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("address");
        if (v != null) {
            setName(v.toString());
        }
        v = args.get("name");
        if (v != null) {
            setName(v.toString());
        }
    }
}
