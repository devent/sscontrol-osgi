package com.anrisoftware.sscontrol.flanneldocker.service.external;

import java.util.List;

import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.misc.external.DebugLogging;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;

/**
 * <i>Flannel-Docker</i> service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface FlannelDocker extends HostService {

    DebugLogging getDebugLogging();

    Binding getBinding();

    Etcd getEtcd();

    Network getNetwork();

    Backend getBackend();

    /**
     * Returns the list of flanneld nodes.
     */
    List<Object> getNodes();

    SshHost getCheckHost();
}
