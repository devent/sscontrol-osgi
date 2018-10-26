package com.anrisoftware.sscontrol.nfs.service.internal;

/*-
 * #%L
 * sscontrol-osgi - collectd-service
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

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.nfs.service.external.Export;
import com.google.inject.assistedinject.Assisted;

/**
 * Directory export.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ExportImpl implements Export {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ExportImplFactory {

        Export create(Map<String, Object> args);
    }

    private String dir;

    @Inject
    ExportImpl(@Assisted Map<String, Object> args) {
        parseDir(args);
    }

    private void parseDir(Map<String, Object> args) {
        Object v = args.get("dir");
        this.dir = v.toString();
    }

    @Override
    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
