/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-command-shell-openssh.
 *
 * sscontrol-osgi-command-shell-openssh is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-command-shell-openssh is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-command-shell-openssh. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.shell.internal.scp;

import static com.anrisoftware.sscontrol.shell.internal.scp.AbstractFileWorkerLogger.m.command_finished_debug;
import static com.anrisoftware.sscontrol.shell.internal.scp.AbstractFileWorkerLogger.m.command_finished_info;
import static com.anrisoftware.sscontrol.shell.internal.scp.AbstractFileWorkerLogger.m.command_finished_trace;

import java.util.Map;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link AbstractFileWorker}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class AbstractFileWorkerLogger extends AbstractLogger {

    enum m {

        command_finished_trace("Command finished: {} for {}, {}."),

        command_finished_debug("Command finished: for {}."),

        command_finished_info("Command finished for '{}'.");

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
     * Sets the context of the logger to {@link AbstractFileWorker}.
     */
    public AbstractFileWorkerLogger() {
        super(AbstractFileWorker.class);
    }

    void commandFinished(Object parent, ProcessTask task,
            Map<String, Object> args) {
        if (isTraceEnabled()) {
            trace(command_finished_trace, args, parent, task);
        } else if (isDebugEnabled()) {
            debug(command_finished_debug, args, parent);
        } else {
            info(command_finished_info, parent);
        }
    }

}
