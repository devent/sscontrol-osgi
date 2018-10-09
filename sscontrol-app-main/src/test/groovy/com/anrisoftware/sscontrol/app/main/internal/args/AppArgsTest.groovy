package com.anrisoftware.sscontrol.app.main.internal.args

import javax.inject.Inject

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.sscontrol.app.main.internal.args.AppArgs.AppArgsFactory
import com.google.inject.Guice

/**
 * @see AppArgs
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class AppArgsTest {

    @Inject
    AppArgsFactory argsFactory

    @Test
    void "no args"() {
        def a = []
        def args = argsFactory.create().parse(a as String[])
        assert args.rootPaths.size() == 0
    }

    @Test
    void "root path"() {
        def a = [folder.newFile('rootPath')]
        def args = argsFactory.create().parse(a as String[])
        assert args.rootPaths.size() == 1
    }

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    @Before
    void injectDependencies() {
        Guice.createInjector(new ArgsModule()).injectMembers(this)
    }
}
