package com.anrisoftware.sscontrol.repo.git.service.internal;

/*-
 * #%L
 * sscontrol-osgi - repo-git-service
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

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.core.resources.ToURI;
import com.anrisoftware.sscontrol.repo.git.service.external.Remote;
import com.google.inject.assistedinject.Assisted;

/**
 * Repository remote host.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class RemoteImpl implements Remote {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface RemoteImplFactory {

        Remote create(Map<String, Object> args);

    }

    private URI uri;

    private transient RemoteImplLogger log;

    @Inject
    RemoteImpl(RemoteImplLogger log, @Assisted Map<String, Object> args) {
        this.log = log;
        parseArgs(args);
    }

    public void setUri(URI uri) {
        this.uri = uri;
        log.uriSet(this, uri);
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("url");
        setUri(ToURI.toURI(v).withScp().convert());
    }

}
