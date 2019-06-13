/**
 * Copyright © 2016 Erwin Müller (erwin.mueller@anrisoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_13;

import com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_13.StDirTemplateParser;
import com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_13.StgFileTemplateParser;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
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
