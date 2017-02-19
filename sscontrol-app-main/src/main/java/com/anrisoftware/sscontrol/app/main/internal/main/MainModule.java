package com.anrisoftware.sscontrol.app.main.internal.main;

import com.anrisoftware.sscontrol.app.main.internal.args.ArgsModule;
import com.google.inject.AbstractModule;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class MainModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new ArgsModule());
    }

}
