/*-
 * #%L
 * sscontrol-osgi - collectd-script-collectd-5-7
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
package com.anrisoftware.sscontrol.haproxy.script.haproxy_1_8.external

import javax.inject.Inject

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.haproxy.service.external.HAProxy
import com.anrisoftware.sscontrol.haproxy.service.external.Proxy

import groovy.util.logging.Slf4j

/**
 * HAProxy 1 8.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class HAProxy_1_8 extends ScriptBase {

    TemplateResource exportsTemplate

    @Inject
    Map<String, ApplyProxyDefaults> applyProxyDefaults
    
    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create('HAProxy_1_8_Templates')
        this.exportsTemplate = templates.getResource('haproxy_config')
    }

    /**
     * Setups the default options for hosts without options.
     */
    def setupDefaultOptions() {
        HAProxy service = this.service
        service.proxies.each { Proxy proxy ->
            applyProxyDefaults[proxy.name].applyDefaults(this, proxy)
        }
    }
    
    def deployConfig() {
        template privileged: true, resource: exportsTemplate, name: 'haproxyConfig', vars: [:], dest: configFile call()
    }

    @Override
    def getLog() {
        log
    }
}
