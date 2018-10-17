/*-
 * #%L
 * sscontrol-osgi - shell-test
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
package com.anrisoftware.sscontrol.shell.external.utils

import static org.junit.jupiter.api.extension.ConditionEvaluationResult.*

import org.junit.jupiter.api.extension.ConditionEvaluationResult
import org.junit.jupiter.api.extension.ExecutionCondition
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * Checks that a socket is available for tests.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractSocketCondition implements ExecutionCondition {

    def final socket
    
    AbstractSocketCondition(def socket) {
        this.socket = socket
    }

    @Override
    ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        evaluateSocket socket
    }

    /**
     * Checks if the socket is available.
     */
    ConditionEvaluationResult evaluateSocket(def socket) {
        checkSocket(socket) ? enabled("Socket ${socket} available.") : disabled("Socket ${socket} not available.")
    }

    /**
     * Checks if the socket is available.
     */
    boolean checkSocket(def socket) {
        new File(socket).exists()
    }
}
