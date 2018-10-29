package com.anrisoftware.sscontrol.nfs.script.centos.internal.centos_7

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.nfs.script.nfs_1_3.external.Nfs_1_3

import groovy.util.logging.Slf4j

import static com.anrisoftware.sscontrol.nfs.script.centos.internal.centos_7.Nfs_1_3_Centos_7_Service.*

import javax.inject.Inject

/**
 * Nfs 1.3 on CentOS systems.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Nfs_1_3_Impl extends Nfs_1_3 {

    @Inject
    Nfs_1_3_Centos_7_Properties propertiesProvider
    
    @Override
    ContextProperties getDefaultProperties() {
        propertiesProvider.get()
    }

    @Override
    def run() {
    }

    @Override
    def getLog() {
        log
    }

    @Override
    String getSystemName() {
        SYSTEM_NAME
    }

    @Override
    String getSystemVersion() {
        SYSTEM_VERSION
    }
}
