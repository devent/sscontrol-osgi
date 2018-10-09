package com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_8.linux;

import java.util.List;

import com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_8.linux.Addresses;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface AddressesFactory {

    Addresses create(Object parent, List<Object> addresses);
}
