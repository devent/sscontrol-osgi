package com.anrisoftware.sscontrol.docker.service.internal;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.docker.service.external.LoggingDriver;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Defines the logging driver. See
 * <a href="https://docs.docker.com/config/containers/logging/">View logs for a
 * container or service.</a>
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class LoggingDriverImpl implements LoggingDriver {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface LoggingDriverImplFactory {

        LoggingDriver create();

        LoggingDriver create(Map<String, Object> args);

    }

    private final LoggingDriverImplLogger log;

    private String driver;

    private final Map<String, Object> opts;

    @AssistedInject
    LoggingDriverImpl(LoggingDriverImplLogger log) throws URISyntaxException {
        this(log, new HashMap<String, Object>());
    }

    @AssistedInject
    LoggingDriverImpl(LoggingDriverImplLogger log,
            @Assisted Map<String, Object> args) throws URISyntaxException {
        this.log = log;
        this.opts = new HashMap<>();
        parseArgs(args);
    }

    public void setDriver(String driver) {
        log.driverSet(this, driver);
        this.driver = driver;
    }

    @Override
    public String getDriver() {
        return driver;
    }

    public void putAllOpts(Map<String, Object> opts) {
        log.optsSet(this, opts);
        this.opts.putAll(opts);
    }

    @Override
    public Map<String, Object> getOpts() {
        return opts;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        parseDriver(args);
        parseOpts(args);
    }

    private void parseOpts(Map<String, Object> args) {
        args.remove("driver");
        putAllOpts(args);
    }

    private void parseDriver(Map<String, Object> args) {
        Object a = args.get("driver");
        if (a != null) {
            setDriver(a.toString());
        }
    }

}
