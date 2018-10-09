package com.anrisoftware.sscontrol.collectd.script.centos.internal.centos_7

import static com.anrisoftware.sscontrol.collectd.script.centos.internal.centos_7.Collectd_Centos_7_Service.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.collectd.script.centos.external.Collectd_Centos
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtilsFactory

import groovy.util.logging.Slf4j

/**
 * Collectd for CentOS 7.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Collectd_Centos_7 extends Collectd_Centos {

    @Inject
    Collectd_Centos_7_Properties propertiesProvider

    Collectd_5_7_Centos_7 collectd

    SystemdUtils systemd

    @Inject
    void setCollectd_5_7_Centos_7_Factory(Collectd_5_7_Centos_7_Factory factory) {
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
	collectd.configureSELinux()
	systemd.startServices()
	systemd.enableServices()
    }

    String getCollectdService() {
	properties.getProperty 'collectd_service', defaultProperties
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
