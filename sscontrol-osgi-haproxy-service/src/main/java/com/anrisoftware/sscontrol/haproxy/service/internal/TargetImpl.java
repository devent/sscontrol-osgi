package com.anrisoftware.sscontrol.haproxy.service.internal;

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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.haproxy.service.external.Target;
import com.google.inject.assistedinject.Assisted;

/**
 * Proxy target.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class TargetImpl implements Target {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface TargetImplFactory {

        Target create(Map<String, Object> args);
    }

    private String address;

    private Integer port;

    @Inject
    TargetImpl(@Assisted Map<String, Object> args) {
        parseAddress(args);
        parsePort(args);
    }

    private void parseAddress(Map<String, Object> args) {
        Object v = args.get("address");
        assertThat("address != null", v, notNullValue());
        this.address = v.toString();
    }

    private void parsePort(Map<String, Object> args) {
        Object v = args.get("port");
        assertThat("port != null", v, notNullValue());
        this.port = ((Number) v).intValue();
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getAddress() {
        return address;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
