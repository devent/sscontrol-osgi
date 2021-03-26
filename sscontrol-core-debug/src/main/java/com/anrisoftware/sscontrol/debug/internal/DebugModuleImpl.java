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
package com.anrisoftware.sscontrol.debug.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.core.strings.ToStringService;
import com.anrisoftware.sscontrol.types.misc.external.DebugModule;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Debug logging module.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class DebugModuleImpl implements DebugModule, Map<String, Object> {

    public interface DebugModuleImplFactory {

        DebugModuleImpl create();

        DebugModuleImpl create(@Assisted DebugModule module);

    }

    @Inject
    private DebugModuleImplLogger log;

    @Inject
    private DebugModuleImplFactory moduleFactory;

    @Inject
    private ToStringService toStringService;

    private Map<String, Object> properties;

    @AssistedInject
    DebugModuleImpl() {
        this.properties = new HashMap<String, Object>();
    }

    @AssistedInject
    DebugModuleImpl(@Assisted DebugModule module) {
        this.properties = new HashMap<String, Object>(module.getProperties());
    }

    public void debug(Map<String, Object> args, String name) {
        args.put("name", name);
        debug(args);
    }

    public void debug(Map<String, Object> args) {
        String name = toStringService.toString(args, "name");
        this.properties = new HashMap<String, Object>(args);
        properties.put("name", name);
        log.debugSet(this, args);
    }

    @Override
    public DebugModule rename(String name) {
        DebugModuleImpl module = moduleFactory.create(this);
        module.properties.put("name", name);
        return module;
    }

    @Override
    public String getName() {
        return (String) properties.get("name");
    }

    @Override
    public int getLevel() {
        return ((Number) properties.get("level")).intValue();
    }

    @Override
    public DebugModule putProperty(String property, Object value) {
        DebugModuleImpl module = moduleFactory.create(this);
        module.properties.put(property, value);
        log.propertyPut(this, property, value);
        return module;
    }

    @Override
    public Map<String, Object> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("properties", properties)
                .toString();
    }

    @Override
    public int size() {
        return properties.size();
    }

    @Override
    public boolean isEmpty() {
        return properties.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return properties.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return properties.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return properties.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return properties.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return properties.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        properties.putAll(m);
    }

    @Override
    public void clear() {
        properties.clear();
    }

    @Override
    public Set<String> keySet() {
        return properties.keySet();
    }

    @Override
    public Collection<Object> values() {
        return properties.values();
    }

    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        return properties.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return properties.equals(o);
    }

    @Override
    public int hashCode() {
        return properties.hashCode();
    }

}
