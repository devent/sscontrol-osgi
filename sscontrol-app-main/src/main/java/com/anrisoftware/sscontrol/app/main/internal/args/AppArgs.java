/**
 * Copyright © 2016 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.app.main.internal.args;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.anrisoftware.globalpom.core.resources.StringToURI;

/**
 * 
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class AppArgs {

    /**
     * 
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
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
