package com.anrisoftware.sscontrol.shell.internal.facts;

import static org.apache.commons.lang3.StringUtils.split;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class CatReleaseParse implements Callable<CatReleaseParse> {

    public interface CatReleaseParseFactory {

        CatReleaseParse create(@Assisted String output);

    }

    private final String output;

    @Inject
    CatReleaseParse(@Assisted String output) {
        this.output = output;
    }

    @Override
    public CatReleaseParse call() throws Exception {
        String[] lines = split(output, '\n');
        for (String line : lines) {
            String[] value = split(line, '=');
            if (value.length != 2) {
                continue;
            }
            switch (value[0]) {
            case "NAME":

                break;

            default:
                break;
            }
        }
        // TODO Auto-generated method stub
        return null;
    }
}
