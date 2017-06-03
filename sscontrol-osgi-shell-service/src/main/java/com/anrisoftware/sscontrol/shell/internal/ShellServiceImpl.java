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
package com.anrisoftware.sscontrol.shell.internal;

import static com.google.inject.Guice.createInjector;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.shell.external.Shell;
import com.anrisoftware.sscontrol.shell.external.ShellService;
import com.anrisoftware.sscontrol.shell.internal.ShellImpl.ShellImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;

/**
 * Creates the shell service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(HostServiceService.class)
public class ShellServiceImpl implements ShellService {

    static final String SERVICE_NAME = "shell";

    @Inject
    private ShellImplFactory serviceFactory;

    @Override
    public String getName() {
        return SERVICE_NAME;
    }

    @Override
    public Shell create(Map<String, Object> args) {
        return (Shell) serviceFactory.create(args);
    }

    @Activate
    protected void start() {
        createInjector(new ShellModule()).injectMembers(this);
    }
}
