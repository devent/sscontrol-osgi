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
package com.anrisoftware.sscontrol.k8smaster.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8sbase.base.external.FlannelPlugin;
import com.anrisoftware.sscontrol.k8sbase.base.internal.FlannelPluginImplLogger;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Flannel</i> plugin.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class FlannelPluginImpl implements FlannelPlugin {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface FlannelPluginImplFactory extends PluginFactory {

    }

    private String range;

    private final FlannelPluginImplLogger log;

    @AssistedInject
    FlannelPluginImpl(FlannelPluginImplLogger log) {
        this(log, new HashMap<String, Object>());
    }

    @AssistedInject
    FlannelPluginImpl(FlannelPluginImplLogger log,
            @Assisted Map<String, Object> args) {
        this.log = log;
        parseArgs(args);
    }

    @Override
    public String getName() {
        return "flannel";
    }

    public void setRange(String range) {
        this.range = range;
        log.rangeSet(this, range);
    }

    @Override
    public String getRange() {
        return range;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("range");
        if (v != null) {
            setRange(v.toString());
        }
    }

}
