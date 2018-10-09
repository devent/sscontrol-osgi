package com.anrisoftware.sscontrol.utils.st.miscrenderers.external;

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
