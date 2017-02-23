/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.etcd.upstream.external;

import java.util.Locale;

import com.anrisoftware.resources.st.external.AttributeRenderer;

/**
 * Renders a number as a boolean, where 0=false and 1=true.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@SuppressWarnings("serial")
public class NumberTrueRenderer implements AttributeRenderer {

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        if (formatString == null) {
            return o.toString();
        }
        switch (formatString) {
        case "numberTrue":
            return toBoolean((Integer) o);
        }
        return o.toString();
    }

    private String toBoolean(Integer number) {
        switch (number) {
        case 0:
            return "false";
        case 1:
            return "true";
        default:
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Class<?> getAttributeType() {
        return Integer.class;
    }

}
