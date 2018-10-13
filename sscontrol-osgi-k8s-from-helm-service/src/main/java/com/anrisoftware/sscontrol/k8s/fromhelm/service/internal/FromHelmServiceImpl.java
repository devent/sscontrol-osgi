package com.anrisoftware.sscontrol.k8s.fromhelm.service.internal;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.util.Providers.of;

import java.util.Map;

import javax.inject.Inject;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.anrisoftware.globalpom.core.strings.ToStringService;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.K8sService;
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sImpl.K8sImplFactory;
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sModule;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;
import com.google.inject.AbstractModule;

/**
 * From Helm service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component(service = HostServiceService.class)
public class FromHelmServiceImpl implements K8sService {

    @Inject
    private K8sImplFactory sshFactory;

    @Reference
    private ToStringService toStringService;

    @Override
    public String getName() {
        return "from-helm";
    }

    @Override
    public HostService create(Map<String, Object> args) {
        return sshFactory.create(args);
    }

    @Activate
    protected void start() {
        createInjector(new K8sModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(ToStringService.class).toProvider(of(toStringService));
            }
        }).injectMembers(this);
    }
}
