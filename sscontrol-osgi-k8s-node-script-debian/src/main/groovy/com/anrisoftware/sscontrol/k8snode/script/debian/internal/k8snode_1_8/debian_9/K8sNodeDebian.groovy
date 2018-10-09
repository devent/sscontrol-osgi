package com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_8.debian_9

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_9_UtilsFactory

import groovy.util.logging.Slf4j

/**
 * K8s-Master Debian.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class K8sNodeDebian extends ScriptBase {

    @Inject
    K8sNodeDebianProperties debianPropertiesProvider

    K8sNodeUpstreamDebian upstream

    @Inject
    K8sNodeUfwDebianFactory ufwFactory

    DebianUtils debian

    @Inject
    void setDebian(Debian_9_UtilsFactory factory) {
        this.debian = factory.create this
    }

    @Inject
    void setK8sNodeUpstreamDebianFactory(K8sNodeUpstreamDebianFactory factory) {
        this.upstream = factory.create(scriptsRepository, service, target, threads, scriptEnv)
    }

    @Override
    def run() {
        debian.installPackages()
        debian.enableModules()
        upstream.setupDefaults()
        ufwFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        upstream.installKubeadm()
        upstream.createService()
        upstream.joinNode()
        upstream.postInstall()
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
