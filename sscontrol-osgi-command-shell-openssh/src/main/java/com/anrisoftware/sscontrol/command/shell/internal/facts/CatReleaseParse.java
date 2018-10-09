package com.anrisoftware.sscontrol.command.shell.internal.facts;

import static org.apache.commons.lang3.StringUtils.split;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.types.host.external.SystemInfo;
import com.anrisoftware.sscontrol.utils.systemmappings.external.DefaultSystemInfoFactory;
import com.google.inject.assistedinject.Assisted;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class CatReleaseParse implements Callable<SystemInfo> {

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
    private DefaultSystemInfoFactory systemFactory;

    @Inject
    CatReleaseParse(@Assisted String output) {
        this.output = output;
    }

    @Override
    public SystemInfo call() {
        Map<String, Object> args = new HashMap<>();
        String[] lines = split(output, '\n');
        for (String line : lines) {
            String[] value = split(line, '=');
            if (value.length != 2) {
                continue;
            }
            switch (value[0]) {
            case "ID":
                args.put("name", parseName(value[1]));
                break;
            case "VERSION_ID":
                args.put("version", value[1].replaceAll("\"", ""));
                break;
            default:
                break;
            }
        }
        return systemFactory.create(args);
    }

    private String parseName(String string) {
        String name = string.replaceAll("\"", "");
        return name;
    }
}
