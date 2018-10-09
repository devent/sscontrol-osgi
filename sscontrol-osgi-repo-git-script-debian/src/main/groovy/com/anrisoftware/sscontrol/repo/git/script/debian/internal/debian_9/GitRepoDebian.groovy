package com.anrisoftware.sscontrol.repo.git.script.debian.internal.debian_9

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.repo.git.script.debian.internal.linux.GitRepoLinux
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_9_UtilsFactory

import groovy.util.logging.Slf4j

/**
 * <i>Git</i> code repository service for Debian.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class GitRepoDebian extends GitRepoLinux {

    @Inject
    GitRepoDebianProperties debianPropertiesProvider

    DebianUtils debian

    @Inject
    void setDebianFactory(Debian_9_UtilsFactory factory) {
        this.debian = factory.create(this)
    }

    @Override
    def run() {
        debian.installPackages()
        super.run()
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
