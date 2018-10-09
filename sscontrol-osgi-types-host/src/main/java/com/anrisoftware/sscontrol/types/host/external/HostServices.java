package com.anrisoftware.sscontrol.types.host.external;

import java.util.List;
import java.util.Set;

/**
 * Host services repository.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface HostServices {

    <T extends HostServiceService> T getAvailableService(String name);

    Set<String> getAvailableServices();

    void putAvailableService(String name, HostServiceService service);

    void removeAvailableService(String name);

    <T extends PreHostService> T getAvailablePreService(String name);

    void putAvailablePreService(String name, PreHostService service);

    void removeAvailablePreService(String name);

    <T extends HostServiceScriptService> T getAvailableScriptService(
            ScriptInfo info);

    void putAvailableScriptService(ScriptInfo info,
            HostServiceScriptService service);

    void removeAvailableScriptService(ScriptInfo name);

    Set<ScriptInfo> getAvailableScriptServices();

    void addService(String name, HostService service);

    void removeService(String name, int index);

    Set<String> getServices();

    List<HostService> getServices(String name);

    void putState(String name, Object state);

    <T> T getState(String name);

    HostTargets<?, ?> getTargets();

    HostTargets<?, ?> getClusters();
}
