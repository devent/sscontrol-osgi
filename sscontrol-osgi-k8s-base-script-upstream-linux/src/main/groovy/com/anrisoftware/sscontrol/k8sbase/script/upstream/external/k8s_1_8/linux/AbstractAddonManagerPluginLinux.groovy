package com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_8.linux

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_8.AbstractKubectlLinux

/**
 * Installs the addon-manager.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractAddonManagerPluginLinux extends ScriptBase {

    /**
     * Returns the run kubectl for the cluster.
     */
    abstract AbstractKubectlLinux getKubectlCluster()
}
