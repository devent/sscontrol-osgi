package com.anrisoftware.sscontrol.runner.groovy.internal;

import static com.google.inject.Guice.createInjector;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.anrisoftware.sscontrol.runner.groovy.internal.RunScriptImpl.RunScriptImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServices;
import com.anrisoftware.sscontrol.types.run.external.RunScript;
import com.anrisoftware.sscontrol.types.run.external.RunScriptService;
import com.google.inject.AbstractModule;

/**
 * Script runner service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component(service = RunScriptService.class)
public class RunScriptServiceImpl implements RunScriptService {

    @Inject
    private RunScriptImplFactory runScriptFactory;

    @Override
    public RunScript create(ExecutorService threads, HostServices repository) {
        return runScriptFactory.create(threads, repository);
    }

    @Activate
    protected void start() {
        createInjector(new RunnerModule(), new AbstractModule() {

            @Override
            protected void configure() {
            }
        }).injectMembers(this);
    }

}
