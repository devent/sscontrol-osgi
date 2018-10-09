package com.anrisoftware.sscontrol.utils.systemmappings.external;

import java.util.Map;

import com.anrisoftware.sscontrol.types.host.external.ScriptInfo;

/**
 * Creates information about the script service.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface DefaultScriptInfoFactory {

    ScriptInfo create(Map<String, Object> args);

    ScriptInfo parse(String name);

}
