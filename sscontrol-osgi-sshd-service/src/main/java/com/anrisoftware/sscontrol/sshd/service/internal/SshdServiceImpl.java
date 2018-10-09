package com.anrisoftware.sscontrol.sshd.service.internal;

import static com.google.inject.Guice.createInjector;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.sshd.service.external.Sshd;
import com.anrisoftware.sscontrol.sshd.service.external.SshdService;
import com.anrisoftware.sscontrol.sshd.service.internal.SshdImpl.SshdImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;

/**
 * Creates the sshd service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(HostServiceService.class)
public class SshdServiceImpl implements SshdService {

    static final String SSHD_NAME = "sshd";

    @Inject
    private SshdImplFactory sshdFactory;

    @Override
    public String getName() {
        return "sshd";
    }

    @Override
    public Sshd create(Map<String, Object> args) {
        return (Sshd) sshdFactory.create(args);
    }

    @Activate
    protected void start() {
        createInjector(new SshdModule()).injectMembers(this);
    }
}
