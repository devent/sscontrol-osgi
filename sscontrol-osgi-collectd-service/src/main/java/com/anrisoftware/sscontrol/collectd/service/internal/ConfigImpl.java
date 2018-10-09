package com.anrisoftware.sscontrol.collectd.service.internal;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.collectd.service.external.Config;
import com.google.inject.assistedinject.Assisted;

/**
 * Collectd configuration.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ConfigImpl implements Config {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ConfigImplFactory {

        Config create(Map<String, Object> args);
    }

    private final String name;

    private String script;

    @Inject
    ConfigImpl(@Assisted Map<String, Object> args) {
        Object v = args.get("name");
        this.name = v.toString();
        v = args.get("script");
        setScript(v.toString());
    }

    @Override
    public String getName() {
        return name;
    }

    public void setScript(String script) {
        this.script = script;
    }

    @Override
    public String getScript() {
        return script;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
