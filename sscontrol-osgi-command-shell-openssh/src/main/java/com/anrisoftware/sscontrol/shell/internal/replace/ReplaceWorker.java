/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-command-shell-openssh.
 *
 * sscontrol-osgi-command-shell-openssh is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-command-shell-openssh is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-command-shell-openssh. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.shell.internal.replace;

import static com.anrisoftware.sscontrol.replace.external.Replace.BEGIN_TOKEN_ARG;
import static com.anrisoftware.sscontrol.replace.external.Replace.END_TOKEN_ARG;
import static com.anrisoftware.sscontrol.replace.external.Replace.REPLACE_ARG;
import static com.anrisoftware.sscontrol.replace.external.Replace.SEARCH_ARG;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import com.anrisoftware.globalpom.core.textmatch.tokentemplate.TokenMarker;
import com.anrisoftware.globalpom.core.textmatch.tokentemplate.TokenTemplate;
import com.anrisoftware.globalpom.core.textmatch.tokentemplate.TokensTemplate;
import com.anrisoftware.globalpom.core.textmatch.tokentemplate.TokensTemplateFactory;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.sscontrol.shell.internal.replace.ParseSedSyntax.ParseSedSyntaxFactory;
import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ReplaceWorker implements Callable<TokensTemplate> {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ReplaceWorkerFactory {

        ReplaceWorker create(@Assisted Map<String, Object> args,
                @Assisted String text);

    }

    private final Map<String, Object> args;

    private final String text;

    @Inject
    private TokensTemplateFactory tokensTemplateFactory;

    @Inject
    ReplaceWorker(@Assisted Map<String, Object> args, @Assisted String text,
            PropertiesProvider propertiesProvider,
            ParseSedSyntaxFactory parseSed) {
        this.args = new HashMap<String, Object>(args);
        this.text = text;
        setupDefaults(propertiesProvider, parseSed);
        checkArgs();
    }

    @Override
    public TokensTemplate call() throws AppException {
        String beginToken = args.get(BEGIN_TOKEN_ARG).toString();
        String endToken = args.get(END_TOKEN_ARG).toString();
        TokenMarker tokens = new TokenMarker(beginToken, endToken);
        String search = args.get(SEARCH_ARG).toString();
        String replace = args.get(REPLACE_ARG).toString();
        TokenTemplate template = new TokenTemplate(args, search, replace);
        return tokensTemplateFactory.create(tokens, template, text).replace();
    }

    private void checkArgs() {
        notNull(args.get(REPLACE_ARG), "%s=null", REPLACE_ARG);
        notNull(args.get(SEARCH_ARG), "%s=null", SEARCH_ARG);
    }

    private void setupDefaults(PropertiesProvider propertiesProvider,
            ParseSedSyntaxFactory parseSed) {
        ContextProperties p = propertiesProvider.getProperties();
        Object replace = args.get(REPLACE_ARG);
        if (replace == null) {
            ParseSedSyntax parser = parseSed
                    .create(args.get(SEARCH_ARG).toString()).parse();
            args.put(SEARCH_ARG, parser.getSearch());
            args.put(REPLACE_ARG, parser.getReplace());
        }
        if (!args.containsKey(BEGIN_TOKEN_ARG)) {
            args.put(BEGIN_TOKEN_ARG, p.getProperty("default_begin_token"));
        }
        if (!args.containsKey(END_TOKEN_ARG)) {
            args.put(END_TOKEN_ARG, p.getProperty("default_end_token"));
        }
    }

}
