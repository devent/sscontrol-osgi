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
package com.anrisoftware.sscontrol.types.groovy.internal

import org.apache.commons.lang3.builder.ToStringBuilder
import org.codehaus.groovy.runtime.InvokerHelper

import com.anrisoftware.sscontrol.types.misc.external.BindingHost

class BindingParent {

    BindingHost binding

    def methodMissing(String name, def args) {
        InvokerHelper.invokeMethod binding, name, args
    }

    def propertyMissing(String name, value) {
        InvokerHelper.setProperty binding, name, value
    }

    def propertyMissing(String name) {
        InvokerHelper.getProperty binding, name
    }

    String toString() {
        ToStringBuilder.reflectionToString(this)
    }
}