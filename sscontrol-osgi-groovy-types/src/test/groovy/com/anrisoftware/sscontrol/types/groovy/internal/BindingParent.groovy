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
