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
package com.anrisoftware.sscontrol.services.internal.ssh;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.services.internal.targets.AbstractTargetsImpl;
import com.anrisoftware.sscontrol.types.ssh.external.Ssh;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.anrisoftware.sscontrol.types.ssh.external.Targets;
import com.anrisoftware.sscontrol.types.ssh.external.TargetsService;

/**
 * Ssh host targets.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class TargetsImpl extends AbstractTargetsImpl<SshHost, Ssh> implements Targets {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface TargetsImplFactory extends TargetsService {

    }

    @Inject
    TargetsImpl() {
    }
}
