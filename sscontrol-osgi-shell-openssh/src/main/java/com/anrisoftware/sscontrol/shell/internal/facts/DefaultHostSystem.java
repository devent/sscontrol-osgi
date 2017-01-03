package com.anrisoftware.sscontrol.shell.internal.facts;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.types.external.HostSystem;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class DefaultHostSystem implements HostSystem {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface DefaultHostSystemFactory {

        DefaultHostSystem create(@Assisted Map<String, Object> args);

    }

    private final String name;

    private final String version;

    @Inject
    DefaultHostSystem(@Assisted Map<String, Object> args) {
        this.name = args.get("name").toString();
        this.version = args.get("version").toString();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name)
                .append("version", version).toString();
    }

}
