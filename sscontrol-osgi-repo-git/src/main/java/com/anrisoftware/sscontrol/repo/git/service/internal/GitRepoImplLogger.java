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
package com.anrisoftware.sscontrol.repo.git.service.internal;

import static com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoImplLogger.m.checkoutSet;
import static com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoImplLogger.m.credentialsSet;
import static com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoImplLogger.m.remoteSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.repo.git.service.external.Checkout;
import com.anrisoftware.sscontrol.repo.git.service.external.Credentials;
import com.anrisoftware.sscontrol.repo.git.service.external.Remote;

/**
 * Logging for {@link GitRepoImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class GitRepoImplLogger extends AbstractLogger {

    enum m {

        remoteSet("Remote {} set for {}"),

        credentialsSet("Credentials {} set for {}"),

        checkoutSet("Checkout {} set for {}");

        private String name;

        private m(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link GitRepoImpl}.
     */
    public GitRepoImplLogger() {
        super(GitRepoImpl.class);
    }

    void credentialsSet(GitRepoImpl repo, Credentials c) {
        debug(credentialsSet, c, repo);
    }

    void remoteSet(GitRepoImpl repo, Remote remote) {
        debug(remoteSet, remote, repo);
    }

    void checkoutSet(GitRepoImpl repo, Checkout checkout) {
        debug(checkoutSet, checkout, repo);
    }

}