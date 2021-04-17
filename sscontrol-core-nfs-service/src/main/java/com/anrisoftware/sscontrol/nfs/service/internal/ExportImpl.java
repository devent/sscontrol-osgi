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
package com.anrisoftware.sscontrol.nfs.service.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.nfs.service.external.Export;
import com.anrisoftware.sscontrol.nfs.service.external.Host;
import com.anrisoftware.sscontrol.nfs.service.internal.HostImpl.HostImplFactory;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;

/**
 * Directory export.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class ExportImpl implements Export {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface ExportImplFactory {

        Export create(Map<String, Object> args);
    }

    private final List<Host> hosts;

    private final ExportImplLogger log;

    @Inject
    private transient HostImplFactory hostFactory;

    private String dir;

    @Inject
    ExportImpl(ExportImplLogger log, @Assisted Map<String, Object> args) {
        this.log = log;
        this.hosts = new ArrayList<>();
        parseDir(args);
    }

    private void parseDir(Map<String, Object> args) {
        Object v = args.get("dir");
        this.dir = v.toString();
    }

    /**
     * <pre>
     * host << "andrea-node-0.muellerpublic.de"
     * </pre>
     */
    public List<String> getHost() {
        return StringListPropertyUtil.stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                Map<String, Object> a = new HashMap<>();
                a.put("name", property);
                host(a);
            }
        });
    }

    /**
     * <pre>
     * host name: "andrea-node-1.muellerpublic.de", options: "rw,sync,no_root_squash"
     * </pre>
     */
    public void host(Map<String, Object> args) {
        Host host = hostFactory.create(args);
        hosts.add(host);
        log.hostAdded(this, host);
    }

    @Override
    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @Override
    public List<Host> getHosts() {
        return hosts;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
