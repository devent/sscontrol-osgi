package com.anrisoftware.sscontrol.runner.groovy.internal;

import java.util.Properties;
import java.util.concurrent.ExecutorService;

import com.anrisoftware.sscontrol.types.external.HostService;
import com.anrisoftware.sscontrol.types.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.external.HostServiceScript;
import com.anrisoftware.sscontrol.types.external.HostServices;
import com.anrisoftware.sscontrol.types.external.SshHost;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class EmptyServiceScript implements HostServiceScript {

    @Override
    public Object run() {
        return null;
    }

    @Override
    public String getSystemName() {
        return null;
    }

    @Override
    public String getSystemVersion() {
        return null;
    }

    @Override
    public Object getLog() {
        return null;
    }

    @Override
    public HostServiceProperties getProperties() {
        return null;
    }

    @Override
    public <T extends ExecutorService> T getThreads() {
        return null;
    }

    @Override
    public Properties getDefaultProperties() {
        return null;
    }

    @Override
    public HostService getService() {
        return null;
    }

    @Override
    public HostServices getScriptsRepository() {
        return null;
    }

    @Override
    public SshHost getTarget() {
        return null;
    }

}
