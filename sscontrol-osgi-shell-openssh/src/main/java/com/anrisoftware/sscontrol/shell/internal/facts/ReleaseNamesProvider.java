package com.anrisoftware.sscontrol.shell.internal.facts;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import com.anrisoftware.resources.texts.external.Texts;
import com.anrisoftware.resources.texts.external.TextsFactory;
import com.opencsv.CSVReader;

/**
 * Returns a mapping of the release names.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ReleaseNamesProvider implements Provider<Map<String, String>> {

    private final Texts texts;

    private Map<String, String> map;

    @Inject
    ReleaseNamesProvider(TextsFactory texts) {
        this.texts = texts.create("Facts");
    }

    public ReleaseNamesProvider load() throws IOException {
        Map<String, String> map = new HashMap<>();
        String res = texts.getResource("release_names").getText();
        try (CSVReader reader = new CSVReader(new StringReader(res))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                map.put(line[0], line[1]);
            }
        }
        this.map = map;
        return this;
    }

    @Override
    public Map<String, String> get() {
        return map;
    }

}
