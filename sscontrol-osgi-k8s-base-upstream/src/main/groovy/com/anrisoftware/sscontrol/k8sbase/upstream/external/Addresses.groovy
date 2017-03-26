package com.anrisoftware.sscontrol.k8sbase.upstream.external

import javax.inject.Inject

import com.anrisoftware.sscontrol.types.external.SshHost
import com.anrisoftware.sscontrol.types.external.Targets
import com.google.inject.assistedinject.Assisted

/**
 * Returns the host, protocol and port of the target.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class Addresses {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    interface AddressesFactory {

        Addresses create(Object parent, Targets targets, List<Object> addresses)
    }

    private Targets targets

    private List<Object> addresses

    private Object parent

    @Inject
    Addresses(@Assisted Object parent, @Assisted Targets targets, @Assisted List<Object> addresses) {
        this.parent = parent
        this.targets = targets
        this.addresses = new ArrayList(addresses)
    }

    List getHosts() {
        targets.inject([]) { result, target ->
            if (target instanceof List) {
                target.each { result << toAddress(target)  }
            }
            else {
                result << toAddress(target)
            }
        }
    }

    private List toAddress(def target) {
        if (target instanceof SshHost) {
            return [host: target.host, protocol: parent.protocol, port: parent.port]
        } else {
            return getAddress(target)
        }
    }

    private List getSshTargets() {
        def targets = targets
        addresses.inject([]) { result, target ->
            targets.getHosts(target).each {
                result << [host: it.host, protocol: parent.protocol, port: parent.port]
            }
            result
        }
    }

    private List getAddress(String address) {
        def uri = new URI(address)
        def host = uri.host ? uri.host : uri.path
        def proto = uri.scheme ? uri.scheme : parent.protocol
        def port = uri.port != -1 ? uri.port : parent.port
        [host: host, protocol: proto, port: port]
    }
}
