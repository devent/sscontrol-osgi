package com.anrisoftware.sscontrol.collectd.script.debian.internal.debian_9

import static com.anrisoftware.sscontrol.collectd.script.debian.internal.debian_9.Collectd_Debian_9_Service.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.collectd.script.debian.external.Collectd_Debian
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtilsFactory

import groovy.util.logging.Slf4j

/**
 * Collectd for Debian 8.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Collectd_Debian_9 extends Collectd_Debian {

    @Inject
    Collectd_Debian_9_Properties propertiesProvider

    Collectd_5_7_Debian_9 collectd

    SystemdUtils systemd

    @Inject
    void setCollectd_5_7_Centos_7_Factory(Collectd_5_7_Debian_9_Factory factory) {
	this.collectd = factory.create(scriptsRepository, service, target, threads, scriptEnv)
    }

    @Inject
    void setSystemdUtilsFactory(SystemdUtilsFactory factory) {
	this.systemd = factory.create(this)
    }

    @Override
    def run() {
	systemd.stopServices()
	installPackages()
	collectd.deployConfiguration()
	systemd.startServices()
	systemd.enableServices()
    }

    @Override
    ContextProperties getDefaultProperties() {
	propertiesProvider.get()
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
