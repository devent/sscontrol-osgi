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
package com.anrisoftware.sscontrol.repo.git.linux.internal.linux

import org.apache.commons.io.IOUtils

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
abstract class GitRepo_Linux extends ScriptBase {

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
            shell """
mkdir -p "${dir}"
cd "${dir}"
$c
git clone ${path} .
""" call()
        } catch (e) {
            shell """
rm -r "${dir}"
""" call()
            throw e
        }
        return dir
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
        if (credentials) {
            def c = "credentials${credentials.type.capitalize()}"(vars)
            return c
        } else {
            return ''
        }
    }

    def credentialsSsh(Map vars) {
        RepoHost repo = vars.repo
        Credentials credentials = repo.repo.credentials
        log.info 'Setup credentials {}', credentials
        File file = new File(vars.dir, "id_rsa")
        File tmp = File.createTempFile("id_rsa", null)
        IOUtils.copy credentials.key.toURL().openStream(), new FileOutputStream(tmp)
        try {
            copy src: tmp, dest: file call()
        } finally {
            tmp.delete()
        }
        """
cat <<EOF > ssh-wrapper
#!/bin/bash
ssh -i "${file}" \$1 \$2
EOF

export GIT_SSH=ssh-wrapper
"""
    }

    @Override
    def getLog() {
        log
    }

    File getCheckoutDirectory() {
        properties.getFileProperty "checkout_directory", base, defaultProperties
    }
}
