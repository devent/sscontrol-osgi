/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.utils.systemmappings.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.anrisoftware.sscontrol.utils.systemmappings.external.AbstractScriptInfo;
import com.anrisoftware.sscontrol.utils.systemmappings.external.DefaultSystemInfoFactory;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 *
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class DefaultScriptInfoImpl extends AbstractScriptInfo {

    @AssistedInject
    DefaultScriptInfoImpl(DefaultSystemInfoFactory systemFactory,
            @Assisted String string) {
        super(parseString(string), systemFactory.parse(string));
    }

    private static String parseString(String string) {
        String[] split = StringUtils.split(string, "/");
        return split[0];
    }

    @AssistedInject
    DefaultScriptInfoImpl(DefaultSystemInfoFactory systemFactory,
            @Assisted Map<String, Object> args) {
        super(getService(args), systemFactory.create(args));
    }

    private static String getService(Map<String, Object> args) {
        Object v = args.get("service");
        assertThat("service=null", v, notNullValue());
        return v.toString().toLowerCase(Locale.ENGLISH);
    }

}
