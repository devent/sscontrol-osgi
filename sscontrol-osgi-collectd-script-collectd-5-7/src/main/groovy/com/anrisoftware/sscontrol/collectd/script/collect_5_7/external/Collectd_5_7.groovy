package com.anrisoftware.sscontrol.collectd.script.collect_5_7.external

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
abstract class Collectd_5_7 extends ScriptBase {

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
