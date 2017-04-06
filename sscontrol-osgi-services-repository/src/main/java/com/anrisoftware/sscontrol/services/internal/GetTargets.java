package com.anrisoftware.sscontrol.services.internal;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.anrisoftware.sscontrol.services.external.NoTargetsForServiceException;
import com.anrisoftware.sscontrol.types.external.HostService;
import com.anrisoftware.sscontrol.types.external.HostTargets;
import com.anrisoftware.sscontrol.types.external.TargetHost;
import com.anrisoftware.sscontrol.types.external.TargetHostService;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class GetTargets<HostType extends TargetHost, TargetType extends TargetHostService<HostType>> {

    private static final String DEFAULT_TARGETS_NAME = "default";

    private final Class<? extends HostType> hostType;

    private final Class<? extends TargetType> targetType;

    private final String argName;

    public GetTargets(Class<? extends HostType> hostType,
            Class<? extends TargetType> targetType, String argName) {
        this.hostType = hostType;
        this.targetType = targetType;
        this.argName = argName;
    }

    @SuppressWarnings("unchecked")
    public List<HostType> parseTarget(HostTargets<HostType, TargetType> targets,
            Map<String, Object> args) {
        Object object = args.get(argName);
        if (hostType.isInstance(object)) {
            return (List<HostType>) Arrays.asList(object);
        }
        if (targetType.isInstance(object)) {
            TargetType t = (TargetType) object;
            return getTargets(targets, t);
        }
        if (object != null) {
            String name = object.toString();
            return getTargets(targets, name);
        } else {
            return getDefaultTargets(targets);
        }
    }

    public List<HostType> getDefaultTargets(
            HostTargets<HostType, TargetType> targets) {
        return getTargets(targets, DEFAULT_TARGETS_NAME);
    }

    public List<HostType> getTargets(HostTargets<HostType, TargetType> targets,
            TargetType target) {
        try {
            return targets.getHosts(target);
        } catch (AssertionError e) {
            throw new NoTargetsForServiceException(e, target.getGroup());
        }
    }

    public List<HostType> getTargets(HostTargets<HostType, TargetType> targets,
            String name) {
        try {
            return targets.getHosts(name);
        } catch (AssertionError e) {
            throw new NoTargetsForServiceException(e, name);
        }
    }

    @SuppressWarnings("unchecked")
    public void setupTargets(HostTargets<HostType, TargetType> targets,
            HostService hostService) {
        if (targetType.isInstance(hostService)) {
            targets.addTarget((TargetType) hostService);
        }
    }

}
