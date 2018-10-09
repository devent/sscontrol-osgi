package com.anrisoftware.sscontrol.types.groovy.internal;

import static com.google.inject.util.Providers.of;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.globalpom.core.strings.ToStringService;
import com.anrisoftware.sscontrol.types.misc.external.BindingHost;
import com.anrisoftware.sscontrol.types.misc.external.BindingHostService;
import com.anrisoftware.sscontrol.types.groovy.internal.BindingHostImpl.BindingHostImplFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

/**
 * Binding host and port service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(BindingHostService.class)
public class BindingHostServiceImpl implements BindingHostService {

    @Reference
    private ToStringService toStringService;

    @Inject
    private BindingHostImplFactory bindingHostFactory;

    @Override
    public BindingHost create() {
        return bindingHostFactory.create();
    }

    @Override
    public BindingHost create(BindingHost binding) {
        return bindingHostFactory.create(binding);
    }

    @Activate
    protected void start() {
        Guice.createInjector(new GroovyTypesModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(ToStringService.class).toProvider(of(toStringService));
            }
        }).injectMembers(this);
    }

}
