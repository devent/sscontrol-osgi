package com.anrisoftware.sscontrol.hosts.service.internal;

/*-
 * #%L
 * sscontrol-osgi - hosts-service
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.hosts.service.external.Host;
import com.google.inject.assistedinject.Assisted;

/**
 * Host with address and aliases.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class HostImpl implements Host {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface HostImplFactory {

        Host create(Map<String, Object> args);

    }

    private final String address;

    private final List<String> aliases;

    private final String host;

    private final String identifier;

    @SuppressWarnings("unchecked")
    @Inject
    HostImpl(@Assisted Map<String, Object> args) {
        this.address = args.get("address").toString();
        this.host = args.get("host").toString();
        this.aliases = new ArrayList<String>(
                (List<String>) args.get("aliases"));
        this.identifier = args.get("identifier").toString();
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
