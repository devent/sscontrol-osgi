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
package com.anrisoftware.sscontrol.k8sbase.base.internal;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8sbase.base.external.Label;
import com.google.inject.assistedinject.Assisted;

/**
 * K8s label.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class LabelImpl implements Label {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface LabelImplFactory {

        Label create(@Assisted Map<String, Object> args);

    }

    private String key;

    private String value;

    private final LabelImplLogger log;

    @Inject
    LabelImpl(LabelImplLogger log, @Assisted Map<String, Object> args) {
        this.log = log;
        parseArgs(args);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("service");
        if (v != null) {
            setServiceRange(v.toString());
        }
        v = args.get("advertise");
        if (v != null) {
            setAdvertiseAddress(v);
        }
        v = args.get("hostname");
        if (v != null) {
            setHostnameOverride(v.toString());
        }
        v = args.get("pod");
        if (v != null) {
            setPodRange(v.toString());
        }
        v = args.get("dns");
        if (v != null) {
            setDnsAddress(v.toString());
        }
        v = args.get("api");
        if (v != null) {
            if (v instanceof List) {
                addAllApiServer((List<?>) v);
            } else {
                addApiServer(v);
            }
        }
    }

}
