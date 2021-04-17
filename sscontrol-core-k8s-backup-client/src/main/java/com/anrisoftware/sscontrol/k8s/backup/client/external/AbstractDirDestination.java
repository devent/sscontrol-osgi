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
package com.anrisoftware.sscontrol.k8s.backup.client.external;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Local directory backup destination.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class AbstractDirDestination implements Destination {

    private transient AbstractDirDestinationLogger log;

    private File dir;

    private String arguments;

    protected AbstractDirDestination(Map<String, Object> args,
            AbstractDirDestinationLogger log) {
        this.log = log;
        parseArgs(args);
    }

    @Override
    public String getType() {
        return "dir";
    }

    public void setDir(File dir) {
        this.dir = dir;
        log.dirSet(this, dir);
    }

    public File getDir() {
        return dir;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
        log.argumentsSet(this, arguments);
    }

    public void setArguments(List<String> arguments) {
        setArguments(StringUtils.join(arguments, " "));
    }

    public String getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        parseName(args);
        parseArguments(args);
    }

    private void parseArguments(Map<String, Object> args) {
        Object v = args.get("arguments");
        if (v != null) {
            if (v instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> list = (List<String>) v;
                setArguments(list);
            } else {
                setArguments(v.toString());
            }
        }
    }

    private void parseName(Map<String, Object> args) {
        Object v = args.get("dir");
        if (v != null) {
            setDir(new File(v.toString()));
        }
    }

}
