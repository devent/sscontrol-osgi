/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
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

import javax.inject.Inject

import org.apache.commons.io.IOUtils

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
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

    TemplateResource gitRepoCommandsTemplate

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create('GitRepoLinuxTemplates')
        this.gitRepoCommandsTemplate = templates.getResource('git_repo_cmds')
    }

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
        Map v = [:] << vars
        RepoHost repo = v.repo
        log.info 'Checkout repository {}', repo
        File dir = v.dir
        if (!dir) {
            dir = checkoutDirectory
            if (!dir) {
                dir = createTmpDir()
            }
        }
        v.dir = dir
        v.script = setupCredentials v
        v.path = toPath repo.repo.remote.uri
        try {
            shell resource: gitRepoCommandsTemplate, name: 'gitCloneScript', vars: v call()
            return dir
        } catch (e) {
            shell """
rm -r "${dir}"
""" call()
            throw e
        } finally {
            shell """
${v.script.script}
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
        Map v = [ret: ret] << vars
        ret.script = gitRepoCommandsTemplate.getText(true, "gitCredentialsSetupScript", "parent", this, "vars", v)
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
