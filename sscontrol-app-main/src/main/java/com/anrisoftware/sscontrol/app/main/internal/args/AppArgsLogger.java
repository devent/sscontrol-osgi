package com.anrisoftware.sscontrol.app.main.internal.args;

import static com.anrisoftware.sscontrol.app.main.internal.args.AppArgsLogger._.rootPathAdded;

import java.net.URL;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link AppArgs}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class AppArgsLogger extends AbstractLogger {

    enum _ {

        rootPathAdded("Root path added: {}");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link AppArgs}.
     */
    public AppArgsLogger() {
        super(AppArgs.class);
    }

    void rootPathAdded(URL p) {
        debug(rootPathAdded, p);
    }
}
