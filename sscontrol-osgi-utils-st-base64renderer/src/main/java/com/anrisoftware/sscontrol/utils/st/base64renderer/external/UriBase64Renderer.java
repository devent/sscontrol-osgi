package com.anrisoftware.sscontrol.utils.st.base64renderer.external;

import java.io.IOException;
import java.net.URI;
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
public class UriBase64Renderer extends StringBase64Renderer {

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        if (formatString == null) {
            return o.toString();
        }
        switch (formatString) {
        case "base64":
            try {
                return toBase64(readResource((URI) o));
            } catch (IOException e) {
                throw new ReadResourceErrorException(e, o, formatString);
            }
        }
        return o.toString();
    }

    private String readResource(URI uri) throws IOException {
        return IOUtils.toString(uri, StandardCharsets.UTF_8);
    }

    @Override
    public Class<?> getAttributeType() {
        return URI.class;
    }

}
