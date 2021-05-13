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
package com.anrisoftware.sscontrol.runner.groovy.internal;

import static org.codehaus.groovy.runtime.InvokerHelper.invokeMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.anrisoftware.sscontrol.types.host.external.HostServiceScriptService;
import com.anrisoftware.sscontrol.types.host.external.HostServices;
import com.anrisoftware.sscontrol.types.host.external.PreHost;
import com.anrisoftware.sscontrol.types.host.external.PreHostService;
import com.anrisoftware.sscontrol.types.host.external.ScriptInfo;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.run.external.RunScript;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.anrisoftware.sscontrol.utils.systemmappings.external.AbstractScriptInfo;
import com.anrisoftware.sscontrol.utils.systemmappings.external.AbstractSystemInfo;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Executes the script.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class RunScriptImpl implements RunScript {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface RunScriptImplFactory {

        RunScriptImpl create(@Assisted ExecutorService threads, @Assisted HostServices services);

    }

    private final HostServices services;

    private final ExecutorService threads;

    private final List<SshHost> defaultTarget;

    @Inject
    private RunScriptImplLogger log;

    @Inject
    private EmptyServiceScript emptyServiceScript;

    @AssistedInject
    RunScriptImpl(@Assisted ExecutorService threads, @Assisted HostServices services) {
        this.threads = threads;
        this.services = services;
        if (services.getTargets().getGroups().contains("default")) {
            this.defaultTarget = getDefaultTarget(services);
        } else {
            this.defaultTarget = new ArrayList<>();
        }
    }

    @SuppressWarnings("unchecked")
    private List<SshHost> getDefaultTarget(HostServices services) {
        return (List<SshHost>) services.getTargets().getHosts("default");
    }

    @Override
    public void run(Map<String, Object> variables) throws AppException {
        for (String name : services.getServices()) {
            List<HostService> ss = services.getServices(name);
            for (int i = 0; i < ss.size(); i++) {
                HostService s = ss.get(i);
                List<? extends TargetHost> targets = getTargets(s);
                for (TargetHost host : targets) {
                    HostServiceScript script;
                    script = createScript(name, s, host, variables);
                    setupScript(variables, script);
                    script.run();
                }
            }
        }
    }

    private HostServiceScript createScript(String name, HostService s, TargetHost host, Map<String, Object> vars)
            throws AppException {
        ScriptInfo info = getSystemScriptName(host, s.getName());
        HostServiceScriptService service = services.getAvailableScriptService(info);
        if (service == null) {
            ScriptInfo i = getLinuxScriptName(name);
            service = services.getAvailableScriptService(i);
            if (service == null) {
                log.scriptNotFound(name, info);
            }
        }
        return createScript(name, s, host, vars, service);
    }

    private HostServiceScript createScript(String name, HostService s, TargetHost host, Map<String, Object> vars,
            HostServiceScriptService service) {
        HostServiceScript script = emptyServiceScript;
        if (service == null) {
            return script;
        }
        script = service.create(services, s, host, threads, vars);
        PreHostService preService = services.getAvailablePreService(name);
        if (preService != null) {
            PreHost pre = preService.create();
            pre.configureServiceScript(script);
        }
        return script;
    }

    private ScriptInfo getLinuxScriptName(String name) {
        return new AbstractScriptInfo(name, new AbstractSystemInfo("linux", "linux", "0") {
        }) {
        };
    }

    private ScriptInfo getSystemScriptName(TargetHost host, String name) {
        SshHost ssh = (SshHost) host;
        return new AbstractScriptInfo(name, ssh.getSystem()) {
        };
    }

    HostServiceScript setupScript(Map<String, Object> args, HostServiceScript script) {
        Object v = args.get("chdir");
        if (v != null) {
            invokeMethod(script, "setChdir", v);
        }
        v = args.get("pwd");
        if (v != null) {
            invokeMethod(script, "setPwd", v);
        }
        v = args.get("sudoEnv");
        if (v != null) {
            invokeMethod(script, "setSudoEnv", args.get("sudoEnv"));
        }
        v = args.get("env");
        if (v != null) {
            invokeMethod(script, "setEnv", args.get("env"));
        }
        return script;
    }

    private List<? extends TargetHost> getTargets(HostService s) {
        return s.getTargets().size() == 0 ? defaultTarget : s.getTargets();
    }

}
