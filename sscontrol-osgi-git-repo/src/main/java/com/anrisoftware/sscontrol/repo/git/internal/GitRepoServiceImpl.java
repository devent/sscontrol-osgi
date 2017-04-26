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
package com.anrisoftware.sscontrol.repo.git.internal;

import static com.google.inject.Guice.createInjector;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.repo.git.external.GitRepoService;
import com.anrisoftware.sscontrol.repo.git.internal.GitRepoImpl.GitRepoImplFactory;
import com.anrisoftware.sscontrol.types.external.host.HostService;
import com.anrisoftware.sscontrol.types.external.host.HostServiceService;

/**
 * <i>Git</i> code repository service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(HostServiceService.class)
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
