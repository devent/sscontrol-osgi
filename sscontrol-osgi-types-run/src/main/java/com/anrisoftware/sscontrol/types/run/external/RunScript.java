package com.anrisoftware.sscontrol.types.run.external;

import java.util.Map;

import com.anrisoftware.sscontrol.types.app.external.AppException;

/**
 * Executes the script.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface RunScript {

    void run(Map<String, Object> variables) throws AppException;
}
