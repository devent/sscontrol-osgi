package com.anrisoftware.sscontrol.parser.groovy.internal;

import static com.google.inject.util.Providers.of;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.BundleContext;

import com.anrisoftware.sscontrol.parser.groovy.internal.ParserImpl.ParserImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServices;
import com.anrisoftware.sscontrol.types.parser.external.Parser;
import com.anrisoftware.sscontrol.types.parser.external.ParserService;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

/**
 * Groovy script parser service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(ParserService.class)
public class ParserServiceImpl implements ParserService {

    @Inject
    private ParserImplFactory scriptsFactory;

    @Override
    public Parser create(URI[] roots, String name, HostServices hostServices) {
        Map<String, Object> variables = new HashMap<String, Object>();
        return scriptsFactory.create(roots, name, variables, hostServices);
    }

    @Override
    public Parser create(URI[] roots, String name,
            Map<String, Object> variables, HostServices hostServices) {
        return scriptsFactory.create(roots, name, variables, hostServices);
    }

    @Activate
    protected void start(final BundleContext bundleContext) {
        Guice.createInjector(new ParserModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(BundleContext.class).toProvider(of(bundleContext));
            }
        }).injectMembers(this);
    }
}
