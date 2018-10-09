package com.anrisoftware.sscontrol.parser.groovy.internal;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.host.external.PreHost;

/**
 * Logging for {@link ParserImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class ParserImplLogger extends AbstractLogger {

    enum m {

        parsedScript("Parsed {} for {}");

        private String name;

        private m(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link ParserImpl}.
     */
    public ParserImplLogger() {
        super(ParserImpl.class);
    }

    void parsedScript(ParserImpl parser, HostService parsedScript) {
        debug(parsedScript, parsedScript, parser);
    }

    AppException errorConfigureCompiler(Exception e,
            PreHost prescript) {
        // TODO Auto-generated method stub
        return null;
    }
}
