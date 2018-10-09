package com.anrisoftware.sscontrol.types.ssh.external

import javax.inject.Inject

import com.anrisoftware.sscontrol.types.host.external.HostService
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.google.inject.assistedinject.Assisted

/**
 * Returns a list of the addresses of the targets.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class TargetsAddressList {

    HostService service

    HostServices repo

    Object parent

    String targetsPropertyName

    @Inject
    TargetsAddressList(
    @Assisted HostService service,
    @Assisted HostServices repo,
    @Assisted String targetsPropertyName,
    @Assisted Object parent) {
        super()
        this.service = service
        this.repo = repo
        this.parent = parent
        this.targetsPropertyName = targetsPropertyName
    }

    List<String> getNodes() {
        List targets = service."${targetsPropertyName}"
        targets.inject([]) { result, e ->
            result.addAll getAddress(e)
            result
        }
    }

    List<String> getAddress(Object n) {
        def list = []
        if (n instanceof String) {
            if (new URI(n).scheme) {
                list << nodeAddressString(n)
            } else {
                list.addAll nodeTargets(n)
            }
        }
        list
    }

    def nodeAddressString(String endpoint) {
        new URI(endpoint).host
    }

    def nodeTargets(String target) {
        repo.targets(target).inject([]) { result, e -> result << e.hostAddress }
    }
}
