/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-command-shell-openssh.
 *
 * sscontrol-osgi-command-shell-openssh is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-command-shell-openssh is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-command-shell-openssh. If not, see <http://www.gnu.org/licenses/>.
 */
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
