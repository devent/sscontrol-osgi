package com.anrisoftware.sscontrol.types.host.external;

import java.util.List;
import java.util.Set;

/**
 * Contains the host targets.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface HostTargets<HostType extends TargetHost, TargetType extends TargetHostService<HostType>> {

    List<HostType> getHosts(String name);

    List<HostType> getHosts(TargetType target);

    Set<String> getGroups();

    void addTarget(TargetType target);

    void removeTarget(String name);

}
