package com.anrisoftware.sscontrol.utils.systemmappings.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.anrisoftware.sscontrol.utils.systemmappings.external.AbstractScriptInfo;
import com.anrisoftware.sscontrol.utils.systemmappings.external.DefaultSystemInfoFactory;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class DefaultScriptInfoImpl extends AbstractScriptInfo {

    @AssistedInject
    DefaultScriptInfoImpl(DefaultSystemInfoFactory systemFactory,
            @Assisted String string) {
        super(parseString(string), systemFactory.parse(string));
    }

    private static String parseString(String string) {
        String[] split = StringUtils.split(string, "/");
        return split[0];
    }

    @AssistedInject
    DefaultScriptInfoImpl(DefaultSystemInfoFactory systemFactory,
            @Assisted Map<String, Object> args) {
        super(getService(args), systemFactory.create(args));
    }

    private static String getService(Map<String, Object> args) {
        Object v = args.get("service");
        assertThat("service=null", v, notNullValue());
        return v.toString().toLowerCase(Locale.ENGLISH);
    }

}
