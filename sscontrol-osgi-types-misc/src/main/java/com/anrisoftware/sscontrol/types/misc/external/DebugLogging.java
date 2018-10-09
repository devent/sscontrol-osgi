package com.anrisoftware.sscontrol.types.misc.external;

import java.util.Map;

/**
 * <p>
 * Debug logging.
 * </p>
 * <h2>Usage Example</h2>
 * 
 * <pre>
 * public void debug(Map<String, Object> args, String name) {
 *     Map<String, Object> arguments = new HashMap<String, Object>(args);
 *     arguments.put("name", name);
 *     invokeMethod(debug, "debug", arguments);
 * }
 * 
 * public void debug(Map<String, Object> args) {
 *     Map<String, Object> arguments = new HashMap<String, Object>(args);
 *     invokeMethod(debug, "debug", arguments);
 * }
 * 
 * &#64;SuppressWarnings("unchecked")
 * public List<Object> getDebug() {
 *     return (List<Object>) invokeMethod(debug, "getDebug", null);
 * }
 * 
 * </pre>
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface DebugLogging {

    /**
     * Returns the modules for which the logging is active.
     */
    Map<String, DebugModule> getModules();

    /**
     * Adds the specified logging module.
     */
    DebugLogging putModule(DebugModule module);

    /**
     * Removes the specified logging module.
     */
    DebugLogging removeModule(String name);

}
