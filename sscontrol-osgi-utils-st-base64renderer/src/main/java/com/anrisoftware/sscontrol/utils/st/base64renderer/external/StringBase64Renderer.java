package com.anrisoftware.sscontrol.utils.st.base64renderer.external;

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
