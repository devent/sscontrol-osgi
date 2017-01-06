/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
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

import static java.lang.String.format;
import static org.codehaus.groovy.runtime.InvokerHelper.invokeMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.types.external.AppException;
import com.anrisoftware.sscontrol.types.external.HostService;
import com.anrisoftware.sscontrol.types.external.HostServiceScript;
import com.anrisoftware.sscontrol.types.external.HostServiceScriptService;
import com.anrisoftware.sscontrol.types.external.HostServices;
import com.anrisoftware.sscontrol.types.external.PreHost;
import com.anrisoftware.sscontrol.types.external.RunScript;
import com.anrisoftware.sscontrol.types.external.SshHost;
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
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface RunScriptImplFactory {

        RunScriptImpl create(@Assisted ExecutorService threads,
                @Assisted HostServices services);

    }

    private final HostServices services;

    private final ExecutorService threads;

    private final List<SshHost> defaultTarget;

    @Inject
    private RunScriptImplLogger log;

    @Inject
    private EmptyServiceScript emptyServiceScript;

    @AssistedInject
    RunScriptImpl(@Assisted ExecutorService threads,
            @Assisted HostServices services) {
        this.threads = threads;
        this.services = services;
        if (services.getTargets().getGroups().contains("default")) {
            this.defaultTarget = services.getTargets().getHosts("default");
        } else {
            this.defaultTarget = new ArrayList<>();
        }
    }

    @Override
    public void run(Map<String, Object> variables) throws AppException {
        for (String name : services.getServices()) {
            List<HostService> ss = services.getServices(name);
            for (int i = 0; i < ss.size(); i++) {
                HostService s = ss.get(i);
                List<SshHost> targets = getTargets(s);
                for (SshHost host : targets) {
                    HostServiceScript script = createScript(name, s, host);
                    setupScript(variables, script);
                    script.run();
                }
            }
        }
    }

    private HostServiceScript createScript(String name, HostService s,
            SshHost host) throws AppException {
        PreHost pre = services.getAvailablePreService(name).create();
        String scriptName = getSystemScriptName(host, name);
        HostServiceScriptService service = services
                .getAvailableScriptService(scriptName);
        if (service == null) {
            log.scriptNotFound(name, scriptName);
            service = services
                    .getAvailableScriptService(getLinuxScriptName(host, name));
        }
        HostServiceScript script = emptyServiceScript;
        if (service == null) {
            return script;
        } else {
            script = service.create(services, s, host, threads);
            pre.configureServiceScript(script);
            return script;
        }
    }

    private String getLinuxScriptName(SshHost host, String name) {
        return format("%s-%s-%s", name, "linux", "0");
    }

    private String getSystemScriptName(SshHost host, String name) {
        String sname = host.getSystem().getName();
        String sversion = host.getSystem().getVersion();
        return format("%s-%s-%s", name, sname, sversion);
    }

    HostServiceScript setupScript(Map<String, Object> args,
            HostServiceScript script) {
        invokeMethod(script, "setChdir", args.get("chdir"));
        invokeMethod(script, "setPwd", args.get("dir"));
        invokeMethod(script, "setSudoEnv", args.get("sudoEnv"));
        invokeMethod(script, "setEnv", args.get("env"));
        return script;
    }

    private List<SshHost> getTargets(HostService s) {
        return s.getTargets().size() == 0 ? defaultTarget : s.getTargets();
    }

}
