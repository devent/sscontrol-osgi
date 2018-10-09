package com.anrisoftware.sscontrol.properties.internal

import static com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.stringListStatement

import javax.inject.Inject

import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService
import com.anrisoftware.sscontrol.types.host.external.HostService
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties
import com.anrisoftware.sscontrol.types.host.external.HostServiceService
import com.anrisoftware.sscontrol.types.host.external.TargetHost
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty
import com.google.inject.assistedinject.Assisted

import groovy.transform.ToString

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@ToString
class PropertiesStub implements HostService {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    interface PropertiesStubFactory {

        PropertiesStub create(Map<String, Object> args)
    }

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    @ToString
    static class PropertiesStubServiceImpl implements HostServiceService {

        @Inject
        PropertiesStubFactory serviceFactory

        @Override
        String getName() {
        }

        @Override
        HostService create(Map<String, Object> args) {
            serviceFactory.create(args)
        }
    }

    HostServiceProperties serviceProperties

    @Inject
    PropertiesStub(HostServicePropertiesService propertiesService, @Assisted Map<String, Object> args) {
        this.serviceProperties = propertiesService.create()
    }

    @Override
    TargetHost getTarget() {
        targets[0]
    }

    @Override
    List<TargetHost> getTargets() {
    }

    @Override
    HostServiceProperties getServiceProperties() {
        serviceProperties
    }

    List<String> getProperty() {
        return stringListStatement(new ListProperty() {

                    @Override
                    public void add(String property) {
                        serviceProperties.addProperty(property)
                    }
                })
    }

    @Override
    String getName() {
        "stub"
    }
}
