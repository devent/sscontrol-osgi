/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-fail2ban.
 *
 * sscontrol-security-fail2ban is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-fail2ban is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-fail2ban. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.fail2ban;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.groovy.statementstable.StatementsTable;
import com.anrisoftware.sscontrol.core.groovy.statementstable.StatementsTableFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Fail2ban</i> service.
 *
 * @see <a href="http://www.fail2ban.org">http://www.fail2ban.org</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Fail2banServiceImpl implements Fail2banService {

    private static final String SERVICE_KEY = "service";

    private static final String DEBUG_KEY = "debug";

    private static final String NAME = "service name";

    /**
     * The <i>Fail2ban</i> service name.
     */
    public static final String SERVICE_NAME = "fail2ban";

    private final List<Jail> jails;

    @Inject
    private Fail2banServiceImplLogger log;

    @Inject
    private JailFactory jailFactory;

    private StatementsTable statementsTable;

    /**
     * @see Fail2banServiceFactory#create(Map)
     */
    @Inject
    Fail2banServiceImpl(@Assisted Map<String, Object> args) {
        this.jails = new ArrayList<Jail>();
    }

    @Inject
    public final void setStatementsTable(StatementsTableFactory factory) {
        StatementsTable table = factory.create(this, SERVICE_NAME);
        table.addAllowed(DEBUG_KEY);
        table.setAllowArbitraryKeys(true, DEBUG_KEY);
        this.statementsTable = table;
    }

    @Override
    public String getName() {
        return SERVICE_NAME;
    }

    @Override
    public Map<String, Object> debugLogging(String key) {
        return statementsTable.tableKeys(DEBUG_KEY, key);
    }

    public void jail(String service) {
        jail(new HashMap<String, Object>(), service, null);
    }

    public Jail jail(Map<String, Object> args, String service, Object s) {
        args.put(SERVICE_KEY, service);
        Jail jail = jailFactory.create(args);
        jails.add(jail);
        log.jailAdded(this, jail);
        return jail;
    }

    @Override
    public List<Jail> getJails() {
        return Collections.unmodifiableList(jails);
    }

    public Object methodMissing(String name, Object args) {
        return statementsTable.methodMissing(name, args);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(NAME, SERVICE_NAME).toString();
    }
}
