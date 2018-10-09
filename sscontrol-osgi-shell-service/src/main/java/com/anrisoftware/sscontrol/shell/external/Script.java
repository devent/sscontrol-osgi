package com.anrisoftware.sscontrol.shell.external;

import java.util.Map;

/**
 * Shell script.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Script {

    Map<String, Object> getVars();
}
