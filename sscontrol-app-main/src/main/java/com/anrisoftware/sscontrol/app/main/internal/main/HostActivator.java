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

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import com.anrisoftware.sscontrol.types.external.host.HostServiceService;
import com.anrisoftware.sscontrol.types.external.parser.ParserService;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class HostActivator implements BundleActivator, BundleListener {

    private BundleContext context;

    private ServiceTracker<ParserService, ServiceReference<ParserService>> parserServiceTracker;

    @Override
    public void start(BundleContext context) {
        this.context = context;
        context.addBundleListener(this);
        this.parserServiceTracker = new ServiceTracker<ParserService, ServiceReference<ParserService>>(
                context, HostServiceService.class.getName(), null) {

            @Override
            public ServiceReference<ParserService> addingService(
                    ServiceReference<ParserService> reference) {
                System.out.println(reference); // TODO println
                // TODO Auto-generated method stub
                return super.addingService(reference);
            }

        };
        parserServiceTracker.open();
    }

    @Override
    public void stop(BundleContext context) {
        parserServiceTracker.close();
        context = null;
    }

    public BundleContext getContext() {
        return context;
    }

    @Override
    public void bundleChanged(BundleEvent event) {
        System.out.println(event); // TODO println
    }
}
