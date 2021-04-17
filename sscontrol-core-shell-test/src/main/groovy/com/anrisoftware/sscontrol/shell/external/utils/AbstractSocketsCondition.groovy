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
package com.anrisoftware.sscontrol.shell.external.utils

import static org.junit.jupiter.api.extension.ConditionEvaluationResult.*

import org.junit.jupiter.api.extension.ConditionEvaluationResult
import org.junit.jupiter.api.extension.ExecutionCondition
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * Checks that the given sockets are all available for tests.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractSocketsCondition implements ExecutionCondition {

    def final sockets

    AbstractSocketsCondition(List sockets) {
        this.sockets = sockets.flatten()
    }

    @Override
    ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        evaluateSockets sockets
    }

    /**
     * Checks if the socket is available.
     */
    ConditionEvaluationResult evaluateSockets(def sockets) {
        checkSockets(sockets) ? enabled("Sockets ${sockets} available.") : disabled("Sockets ${sockets} not available.")
    }

    /**
     * Checks if the socket is available.
     */
    boolean checkSockets(def sockets) {
        def notexists = sockets.find {
            new File(it).exists() == false
        }
        return notexists == null
    }
}
