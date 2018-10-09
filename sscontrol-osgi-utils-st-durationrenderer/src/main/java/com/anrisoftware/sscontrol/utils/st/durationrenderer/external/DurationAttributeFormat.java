package com.anrisoftware.sscontrol.utils.st.durationrenderer.external;

import static com.anrisoftware.globalpom.core.durationsimpleformat.UnitMultiplier.SECONDS;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.inject.Inject;

import org.joda.time.Duration;

import com.anrisoftware.globalpom.core.durationsimpleformat.DurationSimpleFormatFactory;
import com.anrisoftware.resources.st.external.AttributeRenderer;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class DurationAttributeFormat implements AttributeRenderer {

    @Inject
    private DurationSimpleFormatFactory simpleFormatFactory;

    private final NumberFormat format;

    DurationAttributeFormat() {
        this.format = new DecimalFormat("0");
    }

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        Duration duration = (Duration) o;
        if ("seconds".equals(formatString)) {
            return simpleFormatFactory.create(format).format(o, SECONDS);
        } else {
            return Long.toString(duration.getStandardSeconds());
        }
    }

    @Override
    public Class<?> getAttributeType() {
        return Duration.class;
    }

}
