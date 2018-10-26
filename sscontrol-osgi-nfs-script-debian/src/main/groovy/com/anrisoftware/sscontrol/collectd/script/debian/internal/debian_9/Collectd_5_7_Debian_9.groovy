/*-
 * #%L
 * sscontrol-osgi - collectd-script-debian
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
package com.anrisoftware.sscontrol.collectd.script.debian.internal.debian_9

import static com.anrisoftware.sscontrol.collectd.script.debian.internal.debian_9.Collectd_Debian_9_Service.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.collectd.script.collect_5_7.external.Collectd_5_7

import groovy.util.logging.Slf4j

/**
 * Collectd 5.7. for Debian 8.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Collectd_5_7_Debian_9 extends Collectd_5_7 {

    @Inject
    Collectd_Debian_9_Properties propertiesProvider

    @Override
    ContextProperties getDefaultProperties() {
	propertiesProvider.get()
    }

    @Override
    def getLog() {
	log
    }

    @Override
    String getSystemName() {
	SYSTEM_NAME
    }

    @Override
    String getSystemVersion() {
	SYSTEM_VERSION
    }
}
