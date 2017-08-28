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
package com.anrisoftware.sscontrol.k8s.restore.script.internal.script_1_7;

import com.anrisoftware.sscontrol.k8s.backup.client.external.BackupWorker;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Deployment;
import com.anrisoftware.sscontrol.k8s.restore.service.external.Restore;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface RestoreWorkerImplFactory {

    BackupWorker create(Restore service, Deployment deployment);
}
