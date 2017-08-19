/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.k8s.backup.client.external;

import org.joda.time.Duration;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class WaitScalingTimeoutException extends BackupClientException {

    public WaitScalingTimeoutException(String namespace, String name,
            int replicas, Duration timeout) {
        super("Scaling deployment timeout");
        addContextValue("namespace", namespace);
        addContextValue("name", name);
        addContextValue("replicas", replicas);
        addContextValue("timeout", timeout);
    }

    public WaitScalingTimeoutException(Throwable cause, String namespace,
            String name, int replicas, Duration timeout) {
        super("Scaling deployment timeout", cause);
        addContextValue("namespace", namespace);
        addContextValue("name", name);
        addContextValue("replicas", replicas);
        addContextValue("timeout", timeout);
    }
}