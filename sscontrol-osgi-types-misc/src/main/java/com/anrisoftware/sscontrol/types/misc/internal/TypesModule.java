package com.anrisoftware.sscontrol.types.misc.internal;

import com.anrisoftware.sscontrol.types.misc.external.UserPassword;
import com.anrisoftware.sscontrol.types.misc.external.UserPasswordService;
import com.google.inject.AbstractModule;

public class TypesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserPassword.class).to(UserPasswordImpl.class);
        bind(UserPasswordService.class).to(UserPasswordServiceImpl.class);
    }

}
