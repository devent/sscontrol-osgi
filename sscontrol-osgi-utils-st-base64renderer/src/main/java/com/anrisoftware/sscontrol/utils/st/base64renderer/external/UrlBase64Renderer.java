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

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

/**
 * Reads a resource and base64-encodes it if the format string equals to
 * "base64".
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@SuppressWarnings("serial")
public class UrlBase64Renderer extends StringBase64Renderer {

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        if (formatString == null) {
            return o.toString();
        }
        switch (formatString) {
        case "base64":
            try {
                return toBase64(readResource((URL) o));
            } catch (IOException e) {
                throw new ReadResourceErrorException(e, o, formatString);
            }
        }
        return o.toString();
    }

    private String readResource(URL url) throws IOException {
        return IOUtils.toString(url, StandardCharsets.UTF_8);
    }

    @Override
    public Class<?> getAttributeType() {
        return URL.class;
    }

}
