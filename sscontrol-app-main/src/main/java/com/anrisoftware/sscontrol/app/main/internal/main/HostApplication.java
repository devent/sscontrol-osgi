/*
 * Copyright 2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.app.main.internal.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.apache.felix.main.AutoProcessor;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import com.anrisoftware.sscontrol.app.main.internal.args.AppArgs;
import com.anrisoftware.sscontrol.app.main.internal.args.AppArgs.AppArgsFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class HostApplication {

    @Inject
    private HostApplicationLogger log;

    private HostActivator activator;

    private Felix felix;

    private ServiceTracker<HostServiceService, ServiceReference<HostServiceService>> tracker;

    private final AppArgs args;

    private final Map<String, Object> configMap;

    public HostApplication(String[] args) {
        this.configMap = new HashMap<>();
        Injector injector = Guice.createInjector(new MainModule());
        injector.injectMembers(this);
        this.args = injector.getInstance(AppArgsFactory.class).create()
                .parse(args);
    }

    public void setConfigMap(Map<String, Object> map) {
        this.configMap.putAll(map);
    }

    public void start() throws BundleException, InterruptedException {
        activator = new HostActivator();
        List<BundleActivator> list = new ArrayList<>();
        list.add(activator);
        configMap.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, list);
        felix = new Felix(configMap);
        felix.init();
        AutoProcessor.process(null, felix.getBundleContext());
        felix.start();
        this.tracker = new ServiceTracker<>(activator.getContext(),
                HostServiceService.class.getName(), null);
        tracker.open();
        log.felixStarted(configMap);
        felix.waitForStop(0);
    }

    public void stop() throws BundleException, InterruptedException {
        if (felix != null) {
            felix.stop();
            log.felixStopped(felix);
            felix.waitForStop(0);
        }
    }
}
