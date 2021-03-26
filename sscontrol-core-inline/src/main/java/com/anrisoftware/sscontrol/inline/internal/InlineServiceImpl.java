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
package com.anrisoftware.sscontrol.inline.internal;

import static com.google.inject.Guice.createInjector;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.inline.external.Inline;
import com.anrisoftware.sscontrol.inline.external.InlineService;
import com.anrisoftware.sscontrol.inline.internal.InlineImpl.InlineImplFactory;

/**
 * Creates the service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(InlineService.class)
public class InlineServiceImpl implements InlineService {

    static final String SERVICE_NAME = "inline";

    @Inject
    private InlineImplFactory serviceFactory;

    @Override
    public Inline create(Map<String, Object> args) {
        return (Inline) serviceFactory.create(args);
    }

    @Activate
    protected void start() {
        createInjector(new InlineModule()).injectMembers(this);
    }
}
