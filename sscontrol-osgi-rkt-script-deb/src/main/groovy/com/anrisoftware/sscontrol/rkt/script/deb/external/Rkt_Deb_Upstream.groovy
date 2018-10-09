package com.anrisoftware.sscontrol.rkt.script.deb.external

import static org.apache.commons.io.FilenameUtils.getBaseName

import org.apache.commons.io.FilenameUtils

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils

import groovy.util.logging.Slf4j

/**
 * Installs rkt from the upstream sources.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Rkt_Deb_Upstream extends ScriptBase {

    abstract DebianUtils getDebian()

    def installRkt() {
        log.info 'Installs rkt.'
        def installed = debian.checkPackagesVersion([
            [name: rktPackage, version: rktVersion]
        ])
        if (installed) {
            return
        }
        copy src: archive, sig: archiveSig, server: archiveServer, key: archiveKey, dest: "/tmp", direct: true, timeout: timeoutLong call()
        def archiveFile = FilenameUtils.getName(archive.toString())
        def archiveName = getBaseName(getBaseName(archive.toString()))
        shell timeout: timeoutMiddle, """\
cd /tmp
sudo dpkg -i "$archiveFile"
""" call()
    }

    String getRktPackage() {
        properties.getProperty 'rkt_package', defaultProperties
    }

    String getRktVersion() {
        properties.getProperty 'rkt_version', defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
