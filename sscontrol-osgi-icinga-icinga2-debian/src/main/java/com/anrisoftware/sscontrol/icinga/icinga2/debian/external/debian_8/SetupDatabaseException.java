package com.anrisoftware.sscontrol.icinga.icinga2.debian.external.debian_8;

import com.anrisoftware.globalpom.exec.external.core.ProcessTask;
import com.anrisoftware.sscontrol.icinga.service.external.Plugin;
import com.anrisoftware.sscontrol.types.app.external.AppException;

/**
 * Thrown if there was an error setup the database.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@SuppressWarnings("serial")
public class SetupDatabaseException extends AppException {

    public SetupDatabaseException(Plugin plugin, ProcessTask task) {
        super("Error setup database");
        addContextValue("plugin", plugin);
        addContextValue("task", task);
    }
}
