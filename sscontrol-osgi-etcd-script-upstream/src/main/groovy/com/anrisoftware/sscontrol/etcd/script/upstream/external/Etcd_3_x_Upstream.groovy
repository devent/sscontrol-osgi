package com.anrisoftware.sscontrol.etcd.script.upstream.external

import static org.apache.commons.io.FilenameUtils.getBaseName

import org.apache.commons.io.FilenameUtils

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Etcd</i> 3.1 service from the upstream sources.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Etcd_3_x_Upstream extends ScriptBase {

    def installEtcd() {
        log.info 'Installs etcd.'
        copy src: archive, sig: archiveSig, server: archiveServer, key: archiveKey, dest: "/tmp", direct: true, timeout: timeoutLong call()
        def archiveFile = FilenameUtils.getName(archive.toString())
        def archiveName = getBaseName(getBaseName(archive.toString()))
        shell timeout: timeoutMiddle, """\
cd /tmp
tar xf "$archiveFile"
cd "$archiveName"
sudo find . -executable -type f -exec cp '{}' '$binDir' \\;
sudo chmod o+rx '$binDir'/*
""" call()
    }

    @Override
    def getLog() {
        log
    }
}
