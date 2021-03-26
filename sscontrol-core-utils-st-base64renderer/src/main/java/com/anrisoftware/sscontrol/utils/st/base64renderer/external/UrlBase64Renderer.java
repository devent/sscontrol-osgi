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
package com.anrisoftware.sscontrol.utils.st.base64renderer.external;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;

import com.anrisoftware.resources.st.external.StAttributeRenderer;

/**
 * Reads a resource and base64-encodes it if the format string equals to
 * "base64".
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@SuppressWarnings("serial")
public class UrlBase64Renderer implements StAttributeRenderer<URL> {

    @Inject
    private StringBase64Renderer renderer;

    @Override
    public String toString(URL o, String formatString, Locale locale) {
        if (formatString == null) {
            return o.toString();
        }
        switch (formatString) {
        case "base64":
            try {
                return renderer.toBase64(readResource(o));
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
    public Class<URL> getAttributeType() {
        return URL.class;
    }

}
