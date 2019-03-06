package com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_13;

import com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_13.StDirTemplateParser;
import com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_13.StgFileTemplateParser;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class FileTemplateModule extends AbstractModule {

    @Override
    protected void configure() {
        MapBinder<String, TemplateParser> mapbinder = MapBinder.newMapBinder(binder(), String.class,
                TemplateParser.class);
        mapbinder.addBinding(StDirTemplateParser.TEMPLATE_NAME).to(StDirTemplateParser.class);
        mapbinder.addBinding(StgFileTemplateParser.TEMPLATE_NAME).to(StgFileTemplateParser.class);
    }

}
