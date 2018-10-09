package com.anrisoftware.sscontrol.k8smaster.service.external;

import java.util.List;
import java.util.Map;

import com.anrisoftware.sscontrol.k8sbase.base.service.external.K8s;
import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * <i>K8s-Master</i> service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface K8sMaster extends K8s {

    List<Authentication> getAuthentications();

    List<Authorization> getAuthorizations();

    List<String> getAdmissions();

    /**
     * Returns the address and port for the api-server.
     *
     * @return {@link Binding}
     */
    Binding getBinding();

    Account getAccount();

    /**
     * Returns the list of kubernetes nodes.
     */
    List<Object> getNodes();

    /**
     * Returns the CA certificates for signing generated TLS certificates.
     */
    Tls getCa();

    /**
     * Sets the CA certificates for signing generated TLS certificates.
     *
     * <pre>
     * ca ca: "ca.pem", key: "key.pem"
     * </pre>
     */
    void ca(Map<String, Object> args);

}
