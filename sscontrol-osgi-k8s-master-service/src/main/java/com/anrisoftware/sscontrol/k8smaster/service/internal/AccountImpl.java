package com.anrisoftware.sscontrol.k8smaster.service.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8smaster.service.external.Account;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * ServiceAccount.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class AccountImpl implements Account {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface AccountImplFactory {

        Account create();

        Account create(Map<String, Object> args);
    }

    private transient AccountImplLogger log;

    private Tls tls;

    private transient TlsFactory tlsFactory;

    @AssistedInject
    AccountImpl(AccountImplLogger log, TlsFactory tlsFactory) {
        this(log, tlsFactory, new HashMap<String, Object>());
    }

    @AssistedInject
    AccountImpl(AccountImplLogger log, TlsFactory tlsFactory,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.tlsFactory = tlsFactory;
        this.tls = tlsFactory.create(new HashMap<String, Object>());
        parseArgs(args);
    }

    @Override
    public Tls getTls() {
        return tls;
    }

    /**
     * <pre>
     * tls ca: "ca.pem", cert: "cert.pem", key: "key.pem"
     * </pre>
     */
    public void tls(Map<String, Object> args) {
        this.tls = tlsFactory.create(args);
        log.tlsSet(this, tls);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("ca");
        if (v != null) {
            tls(args);
        }
        v = args.get("cert");
        if (v != null) {
            tls(args);
        }
        v = args.get("key");
        if (v != null) {
            tls(args);
        }
    }

}
