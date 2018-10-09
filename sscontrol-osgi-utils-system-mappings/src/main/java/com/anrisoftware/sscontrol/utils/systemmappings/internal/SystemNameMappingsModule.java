package com.anrisoftware.sscontrol.utils.systemmappings.internal;

import com.anrisoftware.sscontrol.types.host.external.ScriptInfo;
import com.anrisoftware.sscontrol.types.host.external.SystemInfo;
import com.anrisoftware.sscontrol.utils.systemmappings.external.DefaultScriptInfoFactory;
import com.anrisoftware.sscontrol.utils.systemmappings.external.DefaultSystemInfoFactory;
import com.anrisoftware.sscontrol.utils.systemmappings.external.SystemNameMappings;
import com.anrisoftware.sscontrol.utils.systemmappings.external.SystemNameMappingsPropertiesFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class SystemNameMappingsModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(SystemInfo.class, DefaultSystemInfoImpl.class)
                .build(DefaultSystemInfoFactory.class));
        install(new FactoryModuleBuilder()
                .implement(ScriptInfo.class, DefaultScriptInfoImpl.class)
                .build(DefaultScriptInfoFactory.class));
        install(new FactoryModuleBuilder()
                .implement(SystemNameMappings.class,
                        SystemNameMappingsProperties.class)
                .build(SystemNameMappingsPropertiesFactory.class));
    }

}
