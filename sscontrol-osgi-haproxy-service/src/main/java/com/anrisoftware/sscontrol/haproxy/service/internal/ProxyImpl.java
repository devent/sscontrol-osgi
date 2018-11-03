/*-
 * #%L
 * sscontrol-osgi - collectd-service
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package com.anrisoftware.sscontrol.haproxy.service.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.haproxy.service.external.Proxy;
import com.anrisoftware.sscontrol.haproxy.service.external.Target;
import com.anrisoftware.sscontrol.haproxy.service.internal.TargetImpl.TargetImplFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * HAProxy proxy.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ProxyImpl implements Proxy {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ProxyImplFactory {

        Proxy create(Map<String, Object> args);
    }

    private final ProxyImplLogger log;

    @Inject
    private transient TargetImplFactory targetFactory;

    private String name;

    private Target target;

    @Inject
    ProxyImpl(ProxyImplLogger log, @Assisted Map<String, Object> args) {
        this.log = log;
        parseName(args);
    }

    private void parseName(Map<String, Object> args) {
        Object v = args.get("name");
        assertThat("name != null", v, notNullValue());
        this.name = v.toString();
    }

    /**
     * <pre>
     * target address: "192.168.56.201", port: 30001
     * </pre>
     */
    public void target(Map<String, Object> args) {
        Target target = targetFactory.create(args);
        this.target = target;
        log.targetSet(this, target);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setTarget(Target target) {
        this.target = target;
        log.targetSet(this, target);
    }

    @Override
    public Target getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
