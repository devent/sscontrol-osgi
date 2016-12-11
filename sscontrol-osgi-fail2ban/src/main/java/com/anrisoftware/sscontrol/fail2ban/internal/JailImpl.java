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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Duration;

import com.anrisoftware.sscontrol.fail2ban.external.Backend;
import com.anrisoftware.sscontrol.fail2ban.external.Jail;
import com.anrisoftware.sscontrol.fail2ban.external.Type;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Fail2ban</i> jail.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class JailImpl implements Jail {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface JailImplFactory {

        Jail create(Map<String, Object> args);

    }

    @SuppressWarnings("unchecked")
    @Inject
    JailImpl(@Assisted Map<String, Object> args) {
        this.address = args.get("address").toString();
        this.host = args.get("host").toString();
        this.aliases = new ArrayList<>(
                (List<String>) args.get("aliases"));
        this.identifier = args.get("identifier").toString();
    }

    @Override
    public String getService() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getNotify() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> getIgnoreAddresses() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer getBanningRetries() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Duration getBanningTime() throws ParseException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Backend getBanningBackend() throws ParseException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type getBanningType() throws ParseException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getBanningApp() throws ParseException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(address).append(HOST, host)
                .append(ALIASES, aliases).append(IDENTIFIER, identifier)
                .toString();
    }

}
