/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.runner.groovy.internal;

import java.io.File;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.anrisoftware.sscontrol.types.host.external.HostServices;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;

/**
 * 
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
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
