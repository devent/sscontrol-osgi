/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.flanneldocker.internal;

import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.anrisoftware.sscontrol.types.host.external.PreHost;
import com.anrisoftware.sscontrol.types.host.external.PreHostService;

/**
 * <i>Etcd</i> pre-script.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class FlannelDockerPreScriptImpl implements PreHost {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface K8sMasterPreScriptImplFactory extends PreHostService {

    }

    @Override
    public void configureCompiler(Object compiler) throws AppException {
    }

    @Override
    public void configureServiceScript(HostServiceScript script)
            throws AppException {
    }

}
