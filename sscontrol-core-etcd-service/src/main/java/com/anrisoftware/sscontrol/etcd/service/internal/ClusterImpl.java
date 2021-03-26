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
package com.anrisoftware.sscontrol.etcd.service.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.etcd.service.external.Binding;
import com.anrisoftware.sscontrol.etcd.service.external.BindingFactory;
import com.anrisoftware.sscontrol.etcd.service.external.Cluster;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Etcd</i> cluster.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ClusterImpl implements Cluster {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface ClusterImplFactory {

        Cluster create(String property);

        Cluster create(Map<String, Object> args);

    }

    private final BindingFactory bindingFactory;

    private final ClusterImplLogger log;

    private Binding address;

    private String name;

    @AssistedInject
    ClusterImpl(ClusterImplLogger log, BindingFactory bindingFactory,
            @Assisted String string) {
        this.log = log;
        this.bindingFactory = bindingFactory;
        parseArgs(string);
    }

    @AssistedInject
    ClusterImpl(ClusterImplLogger log, BindingFactory bindingFactory,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.bindingFactory = bindingFactory;
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

    public void setAddress(String address) {
        Map<String, Object> a = new HashMap<>();
        a.put("address", address);
        Binding binding = bindingFactory.create(a);
        log.addressSet(this, binding);
        this.address = binding;
    }

    @Override
    public Binding getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName()).toString();
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("name");
        if (v != null) {
            setName(v.toString());
        }
        v = args.get("address");
        if (v != null) {
            setAddress(v.toString());
        }
    }

    private void parseArgs(String string) {
        String[] s = StringUtils.split(string, '=');
        assertThat("name=address expected", s.length, equalTo(2));
        setName(s[0]);
        setAddress(s[1]);
    }

}
