/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.fail2ban.internal;

import static com.google.inject.Guice.createInjector;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.fail2ban.external.Fail2ban;
import com.anrisoftware.sscontrol.fail2ban.external.Fail2banService;
import com.anrisoftware.sscontrol.fail2ban.internal.Fail2banImpl.Fail2banImplFactory;

/**
 * Creates the hosts service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(Fail2banService.class)
public class Fail2banServiceImpl implements Fail2banService {

    static final String FAIL2BAN_NAME = "fail2ban";

    @Inject
    private Fail2banImplFactory hostsFactory;

    @Override
    public Fail2ban create(Map<String, Object> args) {
        return (Fail2ban) hostsFactory.create(args);
    }

    @Activate
    protected void start() {
        createInjector(new Fail2banModule()).injectMembers(this);
    }
}
