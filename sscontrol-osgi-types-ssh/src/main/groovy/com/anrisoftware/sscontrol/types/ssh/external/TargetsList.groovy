package com.anrisoftware.sscontrol.types.ssh.external

import javax.inject.Inject

import com.anrisoftware.sscontrol.types.host.external.HostService
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.google.inject.assistedinject.Assisted

/**
 * Returns a list of the hosts of the targets.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class TargetsList {

    HostService service

    HostServices repo

    Object parent

    String targetsPropertyName

    @Inject
    TargetsList(
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

    List<SshHost> getNodes() {
        List targets = service."${targetsPropertyName}"
        targets.inject([]) { result, e ->
            result.addAll getAddress(e)
            result
        }
    }

    List<SshHost> getAddress(Object n) {
        def list = []
        if (n instanceof String) {
            if (!isURIName(n)) {
                list.addAll nodeTargets(n)
            }
        }
        if (n instanceof SshHost) {
            list << n
        }
        if (n instanceof List) {
            list.addAll list
        }
        list
    }

    def nodeAddressString(String endpoint) {
        new URI(endpoint).host
    }

    def nodeTargets(String target) {
        repo.targets(target).inject([]) { result, e -> result << e }
    }

    boolean isURIName(String n) {
        new URI(n).scheme
    }
}
