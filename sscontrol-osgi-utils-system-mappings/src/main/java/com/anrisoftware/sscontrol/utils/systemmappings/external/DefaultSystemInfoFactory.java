package com.anrisoftware.sscontrol.utils.systemmappings.external;

import java.util.Map;

import com.anrisoftware.sscontrol.types.host.external.SystemInfo;

/**
 * Creates information about the host system.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface DefaultSystemInfoFactory {

    SystemInfo create(Map<String, Object> args);

    SystemInfo parse(String name);

}
