package com.anrisoftware.sscontrol.muellerpublicde

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

import static com.anrisoftware.sscontrol.muellerpublicde.MuellerpublicdeResources.*
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.*

import com.anrisoftware.sscontrol.shell.external.utils.AbstractSocketsCondition

/**
 * Checks that the sockets to the masters and nodes is available for tests.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class MuellerpublicdeMastersNodesSocketCondition extends AbstractSocketsCondition {

    MuellerpublicdeMastersNodesSocketCondition() {
        super([mastersHosts, nodesHosts])
    }
}
