/**
 * Copyright © 2016 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.services.internal.repo;

import static com.google.inject.Guice.createInjector;

import javax.inject.Inject;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.anrisoftware.sscontrol.services.internal.repo.ReposImpl.ReposImplFactory;
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule;
import com.anrisoftware.sscontrol.types.repo.external.Repos;
import com.anrisoftware.sscontrol.types.repo.external.ReposService;

/**
 * Creates the code repositories.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component(service = ReposService.class)
public class ReposServiceImpl implements ReposService {

    @Inject
    private ReposImplFactory targetsFactory;

    private Repos targets;

    @Override
    public synchronized Repos create() {
        if (targets == null) {
            this.targets = targetsFactory.create();
        }
        return targets;
    }

    @Activate
    protected void start() {
        createInjector(new TargetsModule()).injectMembers(this);
    }
}
