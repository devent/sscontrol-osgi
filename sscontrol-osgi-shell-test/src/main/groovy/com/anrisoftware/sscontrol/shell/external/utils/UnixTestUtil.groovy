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
package com.anrisoftware.sscontrol.shell.external.utils

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.junit.Assume.*

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils

import com.jcabi.ssh.SSH
import com.jcabi.ssh.Shell


/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class UnixTestUtil {

    static void assumeSocketExists(def socket) {
        assumeSocketsExists([socket])
    }

    static void assumeSocketsExists(List sockets) {
        (0..<sockets.size()).each { int i ->
            def socket = new File(sockets[i])
            assumeTrue "${socket} exists", socket.exists()
        }
    }

    static void createEchoCommands(File dir, List<String> names) {
        names.each { createEchoCommand dir, it }
    }

    static String createEchoCommand(File dir, String name) {
        return createCommand(echoCommand, dir, name)
    }

    static String createWhichCommand(File dir) {
        return createCommand(whichCommand, dir, 'which')
    }

    static String createBasenameCommand(File dir) {
        return createCommand(basenameCommand, dir, 'basename')
    }

    static String createIdCommand(File dir) {
        return createCommand(idCommand, dir, 'id')
    }

    static String createCommand(URL command, File dir, String name) {
        def file = new File(dir, name)
        def stream = new FileOutputStream(file)
        IOUtils.copy command.openStream(), stream
        stream.close()
        file.setExecutable true
        return file.absolutePath
    }

    static String createFile(URL resource, File dir, String name) {
        def file = new File(dir, name)
        def stream = new FileOutputStream(file)
        IOUtils.copy resource.openStream(), stream
        stream.close()
        return file.absolutePath
    }

    static void createBashCommand(File dir) {
        def bash = new File("/bin/bash")
        def bashDest = new File(dir, "bash")
        def out = new FileOutputStream(bashDest)
        if (bash.isFile()) {
            IOUtils.copy new FileInputStream(bash), out
        }
        bashDest.setExecutable true
    }

    static String fileToStringReplace(File file) {
        def user = System.getProperty('user.name')
        String str = FileUtils.readFileToString file
        str = str.replaceAll(/junit\d+/, 'junit')
        str = str.replaceAll(/robobee\d+/, 'robobee')
        str = str.replaceAll(/replace\d+/, 'replace')
        str = str.replaceAll(/random\d+/, 'random')
        str = str.replaceAll(/id_rsa\d+/, 'id_rsa')
        str = str.replaceAll(/$user/, 'user')
    }

    static String resourceToString(URL resource) {
        IOUtils.toString resource
    }

    static assertFileResource(Class context, File dir, String name, String res) {
        def file = new File(dir, name)
        assert file.isFile() == true : "File not found: ${file}"
        def resource = context.getResource(res)
        assert resource != null : "Resource not found: ${context}#${res}"
        assertStringContent fileToStringReplace(file), resourceToString(resource)
    }

    static assertStringResource(Class context, String string, String res) {
        def resource = context.getResource(res)
        assert resource != null : "Resource not found: ${context}#${res}"
        assertStringContent string, resourceToString(resource)
    }

    static String readRemoteFile(String file, String host='robobee-test', int port=22, String user='robobee', URL key=robobeeKey) {
        def s = remoteCommand "cat $file", host, port, user, key
        s = s.replaceAll 'tmp\\.[\\w\\d]*', 'tmp.tmp'
        return s
    }

    static String readPrivilegedRemoteFile(String file, String host='robobee-test', int port=22, String user='robobee', URL key=robobeeKey) {
        remoteCommand "sudo cat $file", host, port, user, key
    }

    static def execRemoteFile(String script, InputStream fileStream, String host='robobee-test', int port=22, String user='robobee', URL key=robobeeKey) {
        new Shell.Safe(
                new SSH(host, port, user, key))
                .exec(script,
                fileStream,
                SysStreamsLogger.outputStream,
                SysStreamsLogger.errorStream
                )
    }

    static String checkRemoteFiles(String dir, String host='robobee-test', int port=22, String user='robobee', URL key=robobeeKey) {
        def s = remoteCommand "ls -l $dir", host, port, user, key
        s = s.replaceAll '\\w{3}\\s+\\d+\\s+\\d{4}', 'date'
        s = s.replaceAll '\\w{3}\\s+\\d+\\s+\\d+:\\d+', 'date'
        s = s.replaceAll 'tmp\\.[\\w\\d]*', 'tmp.tmp'
        s = s.replaceAll(/junit\d+/, 'junit')
    }

    static String checkRemoteFilesPrivileged(String dir, String host='robobee-test', int port=22, String user='robobee', URL key=robobeeKey) {
        def s = remoteCommand "sudo ls -l $dir", host, port, user, key
        s = s.replaceAll '\\w{3}\\s+\\d+\\s+\\d{4}', 'date'
        s = s.replaceAll '\\w{3}\\s+\\d+\\s+\\d+:\\d+', 'date'
    }

    static String remoteCommand(String cmd, String host='robobee-test', int port=22, String user='robobee', URL key=robobeeKey) {
        new Shell.Plain(
                new SSH(host, port, user, key))
                .exec(cmd)
    }

    static final URL echoCommand = UnixTestUtil.class.getResource('echo_command.txt')

    static final URL exit1Command = UnixTestUtil.class.getResource('exit_1_command.txt')

    static final URL whichCommand = UnixTestUtil.class.getResource('which_cmd.txt')

    static final URL basenameCommand = UnixTestUtil.class.getResource('basename_cmd.txt')

    static final URL idCommand = UnixTestUtil.class.getResource('id_cmd.txt')

    static final URL robobeeKey = UnixTestUtil.class.getResource('robobee')

    static final URL robobeePub = UnixTestUtil.class.getResource('robobee.pub')
}
