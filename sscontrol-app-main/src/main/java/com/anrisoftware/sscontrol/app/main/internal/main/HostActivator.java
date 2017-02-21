package com.anrisoftware.sscontrol.app.main.internal.main;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import com.anrisoftware.sscontrol.types.external.HostServiceService;
import com.anrisoftware.sscontrol.types.external.ParserService;

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
