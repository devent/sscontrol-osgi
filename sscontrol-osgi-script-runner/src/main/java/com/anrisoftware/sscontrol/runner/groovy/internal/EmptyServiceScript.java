package com.anrisoftware.sscontrol.runner.groovy.internal;

import java.io.File;
import java.util.Map;
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

    public void setChdir(File dir) {
    }

    public void setPwd(File dir) {
    }

    public void setSudoEnv(Map<String, Object> env) {
    }

    public void setEnv(Map<String, Object> env) {
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
