package com.anrisoftware.sscontrol.command.shell.external.ssh;

import static com.anrisoftware.sscontrol.command.shell.external.ssh.AbstractCmdRunLogger.m.command_finished_debug;
import static com.anrisoftware.sscontrol.command.shell.external.ssh.AbstractCmdRunLogger.m.command_finished_info;
import static com.anrisoftware.sscontrol.command.shell.external.ssh.AbstractCmdRunLogger.m.command_finished_trace;

import java.util.Map;

import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link AbstractCmdRun}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AbstractCmdRunLogger extends AbstractLogger {

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
     * Sets the context of the logger to {@link AbstractCmdRun}.
     */
    public AbstractCmdRunLogger() {
        super(AbstractCmdRun.class);
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
