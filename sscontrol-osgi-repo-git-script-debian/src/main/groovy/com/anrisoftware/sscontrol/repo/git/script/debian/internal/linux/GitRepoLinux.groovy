/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.repo.git.script.debian.internal.linux

import org.apache.commons.io.IOUtils
import org.stringtemplate.v4.ST

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.repo.git.service.external.Credentials
import com.anrisoftware.sscontrol.repo.git.service.external.GitRepo
import com.anrisoftware.sscontrol.types.repo.external.RepoHost

import groovy.util.logging.Slf4j

/**
 * <i>Git</i> code repository service for Linux.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class GitRepoLinux extends ScriptBase {

    @Override
    def run() {
        GitRepo service = this.service
        def dir = checkoutRepo repo: service.hosts[0]
        putState "git-${service.group}-dir", dir
    }

    /**
     * Checkouts the repository and returns the base directory.
     */
    File checkoutRepo(Map vars) {
        RepoHost repo = vars.repo
        log.info 'Checkout repository {}', repo
        File dir = vars.dir
        if (!dir) {
            dir = checkoutDirectory
            if (!dir) {
                dir = createTmpDir()
            }
        }
        vars.dir = dir
        def c = setupCredentials vars
        def path = toPath repo.repo.remote.uri
        try {
            def gitCommand = new ST("""\
git clone <if(checkout.branch)>--branch <checkout.branch><elseif(checkout.tag)>--branch <checkout.tag><endif> --depth 1 ${path} .""")
                    .add("checkout", repo.repo.checkout).render()
            shell """
${c.script}
setup
mkdir -p "${dir}"
cd "${dir}"
$gitCommand
""" call()
            return dir
        } catch (e) {
            shell """
rm -r "${dir}"
""" call()
            throw e
        } finally {
            shell """
${c.script}
cleanup
""" call()
        }
    }

    String toPath(URI uri) {
        switch (uri.scheme) {
            case 'file':
                return uri.path
                break
            default:
                return uri.toString()
        }
    }

    def setupCredentials(Map vars) {
        RepoHost repo = vars.repo
        Credentials credentials = repo.repo.credentials
        Map ret = [:]
        if (credentials) {
            ret = "credentials${credentials.type.capitalize()}"(vars)
        }
        if (!ret.script) {
            ret.script = """\
function setup() {
    true
}
function cleanup() {
    true
}
"""
        }
        return ret
    }

    Map credentialsSsh(Map vars) {
        RepoHost repo = vars.repo
        Credentials credentials = repo.repo.credentials
        log.info 'Setup credentials {}', credentials
        Map ret = [:]
        ret.idRsaFile = createTmpFile()
        ret.knownHostsFile = createTmpFile()
        ret.sshWrapper = createTmpFile()
        File tmp = File.createTempFile("id_rsa", null)
        IOUtils.copy credentials.key.toURL().openStream(), new FileOutputStream(tmp)
        try {
            copy src: tmp, dest: ret.idRsaFile call()
        } finally {
            tmp.delete()
        }
        ret.script = """\
function setup() {
    cat <<"EOF" > "${ret.sshWrapper}"
#!/bin/bash
ssh-keyscan ${repo.repo.remote.uri.host} > "${ret.knownHostsFile}"
ssh -o UserKnownHostsFile="${ret.knownHostsFile}" -i "${ret.idRsaFile}" \$1 \$2
EOF
    chmod +x "${ret.sshWrapper}"
    export GIT_SSH="${ret.sshWrapper}"
}

function cleanup() {
    rm ${ret.idRsaFile}
    rm ${ret.knownHostsFile}
    rm ${ret.sshWrapper}
}
"""
        return ret
    }

    @Override
    def getLog() {
        log
    }

    File getCheckoutDirectory() {
        properties.getFileProperty "checkout_directory", base, defaultProperties
    }
}
