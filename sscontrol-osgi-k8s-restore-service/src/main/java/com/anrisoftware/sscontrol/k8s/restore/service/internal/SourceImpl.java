package com.anrisoftware.sscontrol.k8s.restore.service.internal;

/*-
 * #%L
 * sscontrol-osgi - k8s-restore-service
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.k8s.backup.client.external.AbstractSource;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Source;
import com.google.inject.assistedinject.Assisted;

/**
 * Backup source.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class SourceImpl extends AbstractSource {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface SourceImplFactory {

        Source create(Map<String, Object> args);

    }

    @Inject
    SourceImpl(@Assisted Map<String, Object> args) {
        super(args);
    }


}
