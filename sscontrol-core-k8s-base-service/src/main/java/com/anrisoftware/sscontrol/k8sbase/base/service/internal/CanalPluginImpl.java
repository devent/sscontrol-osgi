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
package com.anrisoftware.sscontrol.k8sbase.base.service.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8sbase.base.service.external.CanalPlugin;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Canal</i> plugin.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class CanalPluginImpl implements CanalPlugin {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface CanalPluginImplFactory extends PluginFactory {

    }

    private final CanalPluginImplLogger log;

    private String iface;

    @AssistedInject
    CanalPluginImpl(CanalPluginImplLogger log) {
        this(log, new HashMap<String, Object>());
    }

    @AssistedInject
    CanalPluginImpl(CanalPluginImplLogger log,
            @Assisted Map<String, Object> args) {
        this.log = log;
        parseArgs(args);
    }

    /**
     * <pre>
     * iface name: "enp0s8"
     * </pre>
     */
    public void iface(Map<String, Object> args) {
        Object v = args.get("name");
        assertThat("name!=null", (String) v, not(isEmptyOrNullString()));
        if (v != null) {
            setIface(v.toString());
        }
    }

    @Override
    public String getName() {
        return "canal";
    }

    public void setIface(String iface) {
        this.iface = iface;
        log.ifaceSet(this, iface);
    }

    @Override
    public String getIface() {
        return iface;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        parseIface(args);
    }

    private void parseIface(Map<String, Object> args) {
        Object v = args.get("iface");
        if (v != null) {
            setIface(v.toString());
        }
    }

}
