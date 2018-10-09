package com.anrisoftware.sscontrol.k8s.backup.client.external;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Local directory backup destination.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
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
