package com.anrisoftware.sscontrol.shell.internal.facts;

import static org.apache.commons.lang3.StringUtils.split;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.shell.internal.facts.DefaultHostSystem.DefaultHostSystemFactory;
import com.anrisoftware.sscontrol.types.external.HostSystem;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class CatReleaseParse implements Callable<HostSystem> {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface CatReleaseParseFactory {

        CatReleaseParse create(@Assisted String output);

    }

    private final String output;

    @Inject
    private DefaultHostSystemFactory hostSystemFactory;

    @Inject
    CatReleaseParse(@Assisted String output) {
        this.output = output;
    }

    @Override
    public HostSystem call() {
        Map<String, Object> args = new HashMap<>();
        String[] lines = split(output, '\n');
        for (String line : lines) {
            String[] value = split(line, '=');
            if (value.length != 2) {
                continue;
            }
            switch (value[0]) {
            case "ID":
                args.put("name", value[1]);
                break;
            case "VERSION_ID":
                args.put("version", value[1].replaceAll("\"", ""));
                break;
            default:
                break;
            }
        }
        return hostSystemFactory.create(args);
    }
}
