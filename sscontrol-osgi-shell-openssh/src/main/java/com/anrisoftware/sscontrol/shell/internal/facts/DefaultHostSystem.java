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

    private String name;

    private String version;

    @Inject
    DefaultHostSystem(@Assisted Map<String, Object> args) {
        this.name = args.get("name").toString();
        this.version = args.get("version").toString();
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setVersion(String version) {
        this.version = version;
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
