package com.anrisoftware.sscontrol.fail2ban.service.internal;

import static com.google.inject.Guice.createInjector;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.fail2ban.service.external.Fail2ban;
import com.anrisoftware.sscontrol.fail2ban.service.external.Fail2banService;
import com.anrisoftware.sscontrol.fail2ban.service.internal.Fail2banImpl.Fail2banImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;

/**
 * Creates the hosts service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(HostServiceService.class)
public class Fail2banServiceImpl implements Fail2banService {

    static final String FAIL2BAN_NAME = "fail2ban";

    @Inject
    private Fail2banImplFactory hostsFactory;

    @Override
    public String getName() {
        return "fail2ban";
    }

    @Override
    public Fail2ban create(Map<String, Object> args) {
        return (Fail2ban) hostsFactory.create(args);
    }

    @Activate
    protected void start() {
        createInjector(new Fail2banModule()).injectMembers(this);
    }
}
