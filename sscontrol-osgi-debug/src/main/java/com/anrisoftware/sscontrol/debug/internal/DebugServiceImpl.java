package com.anrisoftware.sscontrol.debug.internal;

import static com.google.inject.util.Providers.of;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.globalpom.core.strings.ToStringService;
import com.anrisoftware.sscontrol.debug.external.DebugService;
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingImpl.DebugLoggingImplFactory;
import com.anrisoftware.sscontrol.types.misc.external.DebugLogging;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

/**
 * Creates the debug logging.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component(immediate = true)
@Service(DebugService.class)
public class DebugServiceImpl implements DebugService {

    @Inject
    private DebugLoggingImplFactory debugFactory;

    @Reference
    private ToStringService toStringService;

    @Override
    public DebugLogging create() {
        return debugFactory.create();
    }

    @Activate
    protected void start() {
        Guice.createInjector(new DebugLoggingModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(ToStringService.class).toProvider(of(toStringService));
            }
        }).injectMembers(this);
    }
}
