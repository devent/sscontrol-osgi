package com.anrisoftware.sscontrol.etcd.service.internal;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.etcd.service.external.BindingFactory;
import com.anrisoftware.sscontrol.etcd.service.external.Client;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Etcd</i> client.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ClientImpl implements Client {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ClientImplFactory {

        Client create(@Assisted Map<String, Object> args);
    }

    private transient TlsFactory tlsFactory;

    private transient ClientImplLogger log;

    private Tls tls;

    @Inject
    ClientImpl(ClientImplLogger log, BindingFactory bindingFactory,
            TlsFactory tlsFactory, @Assisted Map<String, Object> args) {
        this.log = log;
        this.tls = tlsFactory.create(args);
        this.tlsFactory = tlsFactory;
        parseArgs(args);
    }

    /**
     * <pre>
     * client ca: "ca.pem", cert: "cert.pem", key: "key.pem"
     * </pre>
     */
    public void tls(Map<String, Object> args) {
        this.tls = tlsFactory.create(args);
        log.tlsSet(this, tls);
    }

    @Override
    public Tls getTls() {
        return tls;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
    }

}
