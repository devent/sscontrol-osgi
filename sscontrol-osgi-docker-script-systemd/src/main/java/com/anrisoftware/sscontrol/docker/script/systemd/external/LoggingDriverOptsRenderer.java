package com.anrisoftware.sscontrol.docker.script.systemd.external;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.anrisoftware.resources.st.external.AttributeRenderer;

/**
 * Renders log driver options.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public final class LoggingDriverOptsRenderer implements AttributeRenderer {

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        if (formatString == null) {
            return o.toString();
        }
        switch (formatString) {
        case "camelCaseToDash":
            return toStringDriver(o);
        default:
            return o.toString();
        }
    }

    private String toStringDriver(Object o) {
        String[] r = o.toString().split("(?=\\p{Upper})");
        for (int i = 0; i < r.length; i++) {
            String string = r[i];
            r[i] = string.toLowerCase(Locale.ENGLISH);
        }
        return StringUtils.join(r, "-");
    }

    @Override
    public Class<?> getAttributeType() {
        return String.class;
    }

}
