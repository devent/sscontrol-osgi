package com.anrisoftware.sscontrol.tls.internal;

import static com.google.inject.Guice.createInjector;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;
import com.anrisoftware.sscontrol.tls.external.TlsService;

/**
 * TLS certificates service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(TlsService.class)
public class TlsServiceImpl implements TlsService {

    @Inject
    private TlsFactory sshFactory;

    @Override
    public Tls create(Map<String, Object> args) {
        return sshFactory.create(args);
    }

    @Override
    public Tls create() {
        return sshFactory.create();
    }

    @Activate
    protected void start() {
        createInjector(new TlsModule()).injectMembers(this);
    }
}
