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
package com.anrisoftware.sscontrol.k8s.fromrepository.service.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8s.fromrepository.service.external.Crd;
import com.google.inject.assistedinject.Assisted;

/**
 * Custom resource definition.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class CrdImpl implements Crd {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface CrdImplFactory {

        Crd create(Map<String, Object> args);
    }

    private String kind;

    private String version;

    @Inject
    CrdImpl(@Assisted Map<String, Object> args) {
        parseArgs(args);
    }

    @Override
    public String getKind() {
        return kind;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        parseKind(args);
        parseVersion(args);
    }

    private void parseKind(Map<String, Object> args) {
        Object v = args.get("kind");
        assertThat("kind=null", v, notNullValue());
        this.kind = v.toString();
    }

    private void parseVersion(Map<String, Object> args) {
        Object v = args.get("version");
        assertThat("version=null", v, notNullValue());
        this.version = v.toString();
    }

}
