package com.anrisoftware.sscontrol.repo.git.service.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.repo.git.service.external.Credentials;

/**
 * Access credentials.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public abstract class AbstractCredentials implements Credentials {

    protected AbstractCredentials(Map<String, Object> args) {
        parseArgs(args);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("type");
        assertThat("type=null", v, notNullValue());
        assertThat(String.format("type=%s", getType()), v.toString(),
                equalToIgnoringCase(getType()));
    }

}
