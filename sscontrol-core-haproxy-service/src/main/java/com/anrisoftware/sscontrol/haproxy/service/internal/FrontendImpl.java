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
package com.anrisoftware.sscontrol.haproxy.service.internal;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.haproxy.service.external.Frontend;
import com.google.inject.assistedinject.Assisted;

/**
 * Proxy frontend.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class FrontendImpl extends AbstractTarget implements Frontend {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface FrontendImplFactory {

        Frontend create(Map<String, Object> args);
    }

    @Inject
    FrontendImpl(@Assisted Map<String, Object> args) {
        super(args);
    }

}