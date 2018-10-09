package com.anrisoftware.sscontrol.shell.internal;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.shell.external.Script;
import com.google.inject.assistedinject.Assisted;

/**
 * Script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ScriptImpl implements Script {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ScriptImplFactory {

        Script create(Map<String, Object> args);
    }

    private final Map<String, Object> vars;

    @Inject
    ScriptImpl(@Assisted Map<String, Object> args) {
        this.vars = new HashMap<>(args);
    }

    @Override
    public Map<String, Object> getVars() {
        return vars;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("vars", getVars()).toString();
    }

}
