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

import static com.google.inject.Guice.createInjector;

import java.util.Map;

import javax.inject.Inject;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.anrisoftware.sscontrol.repo.git.service.external.GitRepoService;
import com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoImpl.GitRepoImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;

/**
 * <i>Git</i> code repository service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component(service = HostServiceService.class)
public class GitRepoServiceImpl implements GitRepoService {

    @Inject
    private GitRepoImplFactory factory;

    @Override
    public String getName() {
        return "git";
    }

    @Override
    public HostService create(Map<String, Object> args) {
        return factory.create(args);
    }

    @Activate
    protected void start() {
        createInjector(new GitRepoModule()).injectMembers(this);
    }
}
