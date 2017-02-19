package com.anrisoftware.sscontrol.app.main.internal.args;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.anrisoftware.globalpom.resources.StringToURI;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class AppArgs {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface AppArgsFactory {

        AppArgs create();

    }

    private final List<URL> rootPaths;

    @Inject
    private AppArgsLogger log;

    public AppArgs() {
        this.rootPaths = new ArrayList<>();
    }

    public AppArgs parse(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            throw new ParseArgsException(e, args);
        }
        return this;
    }

    @Argument(multiValued = true)
    public void addRootPath(String path) {
        try {
            URL p = StringToURI.toURI(path).toURL();
            rootPaths.add(p);
            log.rootPathAdded(p);
        } catch (Exception e) {
            throw new ParseRootPathException(e);
        }
    }

    public List<URL> getRootPaths() throws MalformedURLException {
        return rootPaths;
    }
}
