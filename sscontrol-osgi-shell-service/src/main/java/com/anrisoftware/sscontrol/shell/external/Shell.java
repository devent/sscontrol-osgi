package com.anrisoftware.sscontrol.shell.external;

import java.util.List;

import com.anrisoftware.sscontrol.types.host.external.HostService;

/**
 * <i>Shell</i> service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Shell extends HostService {

    /**
     * Returns the shell scripts to run.
     */
    List<Script> getScripts();
}
