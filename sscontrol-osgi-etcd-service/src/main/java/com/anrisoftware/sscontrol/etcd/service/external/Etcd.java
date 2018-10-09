package com.anrisoftware.sscontrol.etcd.service.external;

import java.util.List;

import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.misc.external.DebugLogging;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;

/**
 * <i>Etcd</i> service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Etcd extends HostService {

    String getMemberName();

    DebugLogging getDebugLogging();

    List<Binding> getBindings();

    List<Binding> getAdvertises();

    Tls getTls();

    List<Authentication> getAuthentications();

    Peer getPeer();

    Client getClient();

    Proxy getProxy();

    Gateway getGateway();

    SshHost getCheckHost();
}
