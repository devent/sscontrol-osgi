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
package com.anrisoftware.sscontrol.utils.st.durationrenderer.external;

import static com.anrisoftware.globalpom.core.durationsimpleformat.UnitMultiplier.SECONDS;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.inject.Inject;

import org.joda.time.Duration;

import com.anrisoftware.globalpom.core.durationsimpleformat.DurationSimpleFormatFactory;
import com.anrisoftware.resources.st.external.StAttributeRenderer;

/**
 *
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
@SuppressWarnings("serial")
public class DurationAttributeFormat implements StAttributeRenderer<Duration> {

    @Inject
    private DurationSimpleFormatFactory simpleFormatFactory;

    private final NumberFormat format;

    DurationAttributeFormat() {
        this.format = new DecimalFormat("0");
    }

    @Override
    public String toString(Duration o, String formatString, Locale locale) {
        if ("seconds".equals(formatString)) {
            return simpleFormatFactory.create(format).format(o, SECONDS);
        } else {
            return Long.toString(o.getStandardSeconds());
        }
    }

    @Override
    public Class<Duration> getAttributeType() {
        return Duration.class;
    }

}
