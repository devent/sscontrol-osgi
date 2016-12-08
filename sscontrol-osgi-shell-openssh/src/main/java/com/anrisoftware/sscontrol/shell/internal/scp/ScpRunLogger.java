/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-shell-openssh.
 *
 * sscontrol-osgi-shell-openssh is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-shell-openssh is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-shell-openssh. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.shell.internal.scp;

import static com.anrisoftware.sscontrol.shell.internal.scp.ScpRunLogger._.setup_remote_finished_debug;
import static com.anrisoftware.sscontrol.shell.internal.scp.ScpRunLogger._.setup_remote_finished_info;
import static com.anrisoftware.sscontrol.shell.internal.scp.ScpRunLogger._.setup_remote_finished_trace;

import java.util.Map;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link ScpRun}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class ScpRunLogger extends AbstractLogger {

    enum _ {

        setup_remote_finished_trace("Setup remote finished: {} for {}, {}."),

        setup_remote_finished_debug("Setup remote finished: for {}."),

        setup_remote_finished_info("Setup remote finished for '{}'.");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link ScpRun}.
     */
    public ScpRunLogger() {
        super(ScpRun.class);
    }

    void setupRemoteFinished(Object parent, ProcessTask task,
            Map<String, Object> args) {
        if (isTraceEnabled()) {
            trace(setup_remote_finished_trace, args, parent, task);
        } else if (isDebugEnabled()) {
            debug(setup_remote_finished_debug, args, parent);
        } else {
            info(setup_remote_finished_info, parent);
        }
    }

}
