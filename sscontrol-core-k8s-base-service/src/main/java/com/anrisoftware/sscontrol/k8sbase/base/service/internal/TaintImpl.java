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
package com.anrisoftware.sscontrol.k8sbase.base.service.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8sbase.base.service.external.Taint;
import com.google.inject.assistedinject.Assisted;

/**
 * Node taint.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class TaintImpl implements Taint {

    private String key;

    private String value;

    private String effect;

    @Inject
    TaintImpl(@Assisted Map<String, Object> args) {
        parseArgs(args);
    }

    public void setKey(String key) {
        assertThat("key=null", key, not(isEmptyString()));
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    @Override
    public String getEffect() {
        return effect;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("key");
        assertThat("key=null", v, notNullValue());
        setKey(v.toString());
        v = args.get("value");
        if (v != null) {
            setValue(v.toString());
        }
        v = args.get("effect");
        if (v != null) {
            setEffect(v.toString());
        }
    }

}
