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
package com.anrisoftware.sscontrol.collectd.script.collect_5_8.external

import com.anrisoftware.sscontrol.collectd.service.external.Collectd
import com.anrisoftware.sscontrol.collectd.service.external.Config
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.text.SimpleTemplateEngine
import groovy.util.logging.Slf4j

/**
 * Collectd 5.7.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Collectd_5_8 extends ScriptBase {

    @Override
    def run() {
    }

    def deployConfiguration() {
        Collectd service = this.service
        replace privileged: true, dest: configFile with {
            line "s~(?m)^${collectdIncludeLineSearch}~${collectdIncludeLineReplace}~"
            line "s~(?m)^#?FQDNLookup.*~FQDNLookup ${fqdnLookup}~"
            it
        }()
        service.configs.each { Config config ->
            def file = getConfigFile config
            copyString privileged: true, str: config.script, dest: file
        }
    }

    File getConfigFile(Config config) {
        def s = getScriptProperty "config_file_template"
        new File((File)base, new SimpleTemplateEngine().createTemplate(s).make([name: config.name]).toString())
    }

    String getCollectdIncludeLineSearch() {
        getScriptProperty "collectd_include_line_search"
    }

    String getCollectdIncludeLineReplace() {
        getScriptProperty "collectd_include_line_replace"
    }

    boolean getFqdnLookup() {
        getScriptBooleanProperty "collectd_fqdn_lookup"
    }

    @Override
    def getLog() {
        log
    }
}
