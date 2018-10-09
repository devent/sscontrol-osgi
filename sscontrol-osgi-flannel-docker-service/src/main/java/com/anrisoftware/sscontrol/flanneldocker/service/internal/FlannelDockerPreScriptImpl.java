package com.anrisoftware.sscontrol.flanneldocker.service.internal;

import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.anrisoftware.sscontrol.types.host.external.PreHost;
import com.anrisoftware.sscontrol.types.host.external.PreHostService;

/**
 * <i>Etcd</i> pre-script.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class FlannelDockerPreScriptImpl implements PreHost {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface K8sMasterPreScriptImplFactory extends PreHostService {

    }

    @Override
    public void configureCompiler(Object compiler) throws AppException {
    }

    @Override
    public void configureServiceScript(HostServiceScript script)
            throws AppException {
    }

}
