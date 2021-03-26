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
package com.anrisoftware.sscontrol.repo.git.service.internal;

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.core.resources.ToURI;
import com.anrisoftware.sscontrol.repo.git.service.external.SshCredentials;
import com.google.inject.assistedinject.Assisted;

/**
 * SSH credentials.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class SshCredentialsImpl extends AbstractCredentials
        implements SshCredentials {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface SshCredentialsImplFactory extends CredentialsFactory {

    }

    private URI key;

    private final SshCredentialsImplLogger log;

    @Inject
    SshCredentialsImpl(SshCredentialsImplLogger log,
            @Assisted Map<String, Object> args) {
        super(args);
        this.log = log;
        parseArgs(args);
    }

    @Override
    public String getType() {
        return "ssh";
    }

    public void setKey(URI key) {
        this.key = key;
        log.keySet(this, key);
    }

    @Override
    public URI getKey() {
        return key;
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("key");
        setKey(ToURI.toURI(v).convert());
    }

}
