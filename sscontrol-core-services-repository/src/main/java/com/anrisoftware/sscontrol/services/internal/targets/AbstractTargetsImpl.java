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
package com.anrisoftware.sscontrol.services.internal.targets;

import static java.lang.String.format;
import static java.util.Collections.synchronizedList;
import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.types.host.external.HostTargets;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.host.external.TargetHostService;

/**
 * Host targets.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public abstract class AbstractTargetsImpl<HostType extends TargetHost, TargetType extends TargetHostService<HostType>>
        implements HostTargets<HostType, TargetType>, Map<String, List<HostType>> {

    private final List<TargetType> hosts;

    private final Set<String> groups;

    @Inject
    private AbstractTargetsImplLogger log;

    protected AbstractTargetsImpl() {
        this.hosts = synchronizedList(new ArrayList<TargetType>());
        this.groups = new HashSet<>();
    }

    public List<HostType> call(String name) {
        return call(new HashMap<String, Object>(), name);
    }

    public List<HostType> call(Map<String, Object> args, String name) {
        List<HostType> hosts = getHosts(name);
        parseArgs(args);
        return hosts;
    }

    @Override
    public List<HostType> getHosts(TargetType ssh) {
        List<TargetType> list = new ArrayList<>();
        for (TargetType host : hosts) {
            if (host == ssh) {
                list.add(ssh);
            }
        }
        return getHosts(ssh.getGroup(), list);
    }

    @Override
    public List<HostType> getHosts(String name) {
        List<TargetType> targets = searchHosts(name);
        return getHosts(name, targets);
    }

    private List<HostType> getHosts(String name, List<TargetType> targets) {
        assertThat(format("size targets(%s)=0", name), targets.size(), greaterThan(0));
        List<HostType> result = new ArrayList<>();
        for (TargetType target : targets) {
            result.addAll(target.getHosts());
        }
        return result;
    }

    @Override
    public Set<String> getGroups() {
        Set<String> set = new HashSet<>();
        for (TargetType ssh : hosts) {
            set.add(ssh.getGroup());
        }
        return unmodifiableSet(set);
    }

    @Override
    public void addTarget(TargetType ssh) {
        String group = ssh.getGroup();
        hosts.add(ssh);
        groups.add(group);
        log.addHosts(this, ssh, group);
    }

    @Override
    public void removeTarget(String name) {
        if (isBlank(name)) {
            name = "default";
        }
        var n = name;
        hosts.stream().filter(item -> item.getGroup().equals(n)).findFirst().ifPresent(hosts::remove);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("hosts", getGroups()).toString();
    }

    @Override
    public int size() {
        return hosts.size();
    }

    @Override
    public boolean isEmpty() {
        return hosts.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return getGroups().contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return hosts.contains(value);
    }

    @Override
    public List<HostType> get(Object key) {
        return getHosts(key.toString());
    }

    @Override
    public List<HostType> put(String key, List<HostType> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<HostType> remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends String, ? extends List<HostType>> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> keySet() {
        return getGroups();
    }

    @Override
    public Collection<List<HostType>> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<java.util.Map.Entry<String, List<HostType>>> entrySet() {
        Set<java.util.Map.Entry<String, List<HostType>>> set = new HashSet<>();
        for (final String group : groups) {
            final List<HostType> list = new ArrayList<>();
            list.addAll(getHosts(group));
            set.add(new Entry<String, List<HostType>>() {

                @Override
                public List<HostType> setValue(List<HostType> value) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public List<HostType> getValue() {
                    return list;
                }

                @Override
                public String getKey() {
                    return group;
                }
            });
        }
        return set;
    }

    @Override
    public boolean equals(Object o) {
        return hosts.equals(o);
    }

    @Override
    public int hashCode() {
        return hosts.hashCode();
    }

    private Map<String, Object> parseArgs(Map<String, Object> args) {
        Map<String, Object> result = new HashMap<>();
        return unmodifiableMap(result);
    }

    private List<TargetType> searchHosts(String name) {
        List<TargetType> list = new ArrayList<>();
        for (TargetType ssh : hosts) {
            if ("all".equals(name)) {
                list.add(ssh);
                continue;
            }
            if (name.equals(ssh.getGroup())) {
                list.add(ssh);
                continue;
            }
        }
        return list;
    }

}
