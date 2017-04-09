package com.anrisoftware.sscontrol.k8scluster.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8scluster.external.Credentials;

/**
 * Parses the name host and port for the credentials.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public abstract class AbstractCredentials implements Credentials {

    private String name;

    private final AbstractCredentialsLogger log;

    private String host;

    private Integer port;

    protected AbstractCredentials(AbstractCredentialsLogger log,
            Map<String, Object> args) {
        this.log = log;
        parseArgs(args);
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getHost() {
        return host;
    }

    public void setPort(int port) {
        assertThat("port>0", port, greaterThan(0));
        this.port = port;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    public void setName(String name) {
        this.name = name;
        log.nameSet(this, name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("name");
        assertThat("name=null", v, notNullValue());
        setName(v.toString());
        v = args.get("type");
        assertThat("type=null", v, notNullValue());
        assertThat(String.format("type=%s", getType()), v.toString(),
                equalToIgnoringCase(getType()));
        v = args.get("port");
        if (v != null) {
            setPort((Integer) v);
        }
    }

}
