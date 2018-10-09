package com.anrisoftware.sscontrol.collectd.script.centos.external

import javax.inject.Inject

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.centos.external.CentosUtils
import com.anrisoftware.sscontrol.utils.centos.external.Centos_7_UtilsFactory

import groovy.util.logging.Slf4j

/**
 * Collectd on CentOS systems.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Collectd_Centos extends ScriptBase {

    CentosUtils centos

    @Inject
    void setCentosFactory(Centos_7_UtilsFactory factory) {
        this.centos = factory.create(this)
    }

    @Override
    def run() {
    }

    void installPackages() {
        log.info "Installing packages {}.", packages
        centos.installPackages prePackages
        centos.installPackages()
    }

    List getPrePackages() {
        properties.getListProperty 'pre_packages', defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
