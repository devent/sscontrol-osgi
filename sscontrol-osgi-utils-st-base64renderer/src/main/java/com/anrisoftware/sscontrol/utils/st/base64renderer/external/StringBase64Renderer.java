package com.anrisoftware.sscontrol.utils.st.base64renderer.external;

/*-
 * #%L
 * sscontrol-osgi - utils-st-base64renderer
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Locale;

import org.apache.commons.codec.binary.Base64;

import com.anrisoftware.resources.st.external.AttributeRenderer;

/**
 * Base64-encodes a string it if the format string equals to "base64".
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@SuppressWarnings("serial")
public class StringBase64Renderer implements AttributeRenderer {

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        if (formatString == null) {
            return o.toString();
        }
        switch (formatString) {
        case "base64":
            return toBase64(o.toString());
        }
        return o.toString();
    }

    protected String toBase64(String str) {
        byte[] encodedBytes = Base64.encodeBase64(str.getBytes());
        String encoded = new String(encodedBytes);
        return encoded.replaceAll("\n", "");
    }

    @Override
    public Class<?> getAttributeType() {
        return String.class;
    }

}
