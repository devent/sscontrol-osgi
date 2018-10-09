package com.anrisoftware.sscontrol.collectd.script.debian.external

import javax.inject.Inject

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_9_UtilsFactory

import groovy.util.logging.Slf4j

/**
 * Collectd on Debian systems.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Collectd_Debian extends ScriptBase {

    DebianUtils debian

    @Inject
    void setDebianFactory(Debian_9_UtilsFactory factory) {
	this.debian = factory.create(this)
    }

    @Override
    def run() {
    }

    void installPackages() {
	log.info "Installing packages {}.", packages
	debian.installPackages()
    }

    @Override
    def getLog() {
	log
    }
}
