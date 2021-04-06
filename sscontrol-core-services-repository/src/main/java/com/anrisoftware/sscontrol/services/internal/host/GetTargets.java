/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.services.internal.host;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.anrisoftware.sscontrol.services.external.NoTargetsForServiceException;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;
import com.anrisoftware.sscontrol.types.host.external.HostTargets;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.host.external.TargetHostService;

/**
 *
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class GetTargets<HostType extends TargetHost, TargetType extends TargetHostService<HostType>> {

    private static final String DEFAULT_TARGETS_NAME = "default";

    private final Class<? extends HostType> hostType;

    private final Class<? extends TargetType> targetType;

    private final String argName;

    public GetTargets(Class<? extends HostType> hostType, Class<? extends TargetType> targetType, String argName) {
        this.hostType = hostType;
        this.targetType = targetType;
        this.argName = argName;
    }

    @SuppressWarnings("unchecked")
    public List<HostType> parseTarget(HostServiceService service, HostTargets<HostType, TargetType> targets,
            Map<String, Object> args) {
        Object object = args.get(argName);
        if (hostType.isInstance(object)) {
            return (List<HostType>) Arrays.asList((HostType) object);
        }
        if (targetType.isInstance(object)) {
            TargetType t = (TargetType) object;
            return getTargets(service, targets, t);
        }
        if (object != null) {
            String name = object.toString();
            return getTargets(service, targets, name);
        } else {
            return getDefaultTargets(service, targets);
        }
    }

    public List<HostType> getDefaultTargets(HostServiceService service, HostTargets<HostType, TargetType> targets) {
        return getTargets(service, targets, DEFAULT_TARGETS_NAME);
    }

    public List<HostType> getTargets(HostServiceService service, HostTargets<HostType, TargetType> targets,
            TargetType target) {
        try {
            return targets.getHosts(target);
        } catch (AssertionError e) {
            throw new NoTargetsForServiceException(e, service, target.getGroup());
        }
    }

    public List<HostType> getTargets(HostServiceService service, HostTargets<HostType, TargetType> targets,
            String name) {
        try {
            return targets.getHosts(name);
        } catch (AssertionError e) {
            throw new NoTargetsForServiceException(e, service, name);
        }
    }

    @SuppressWarnings("unchecked")
    public void setupTargets(HostTargets<HostType, TargetType> targets, HostService hostService) {
        if (targetType.isInstance(hostService)) {
            targets.addTarget((TargetType) hostService);
        }
    }

}
