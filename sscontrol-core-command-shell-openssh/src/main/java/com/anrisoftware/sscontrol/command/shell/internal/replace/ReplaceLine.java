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
package com.anrisoftware.sscontrol.command.shell.internal.replace;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.command.shell.internal.replace.ParseSedSyntax.ParseSedSyntaxFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class ReplaceLine {

    public interface ReplaceLineFactory {

        ReplaceLine create(Map<String, Object> args);

    }

    private final Map<String, Object> args;

    @Inject
    private ParseSedSyntaxFactory sedFactory;

    @Inject
    ReplaceLine(@Assisted Map<String, Object> args) {
        this.args = new HashMap<String, Object>(args);
    }

    public Map<String, Object> getArgs(Map<String, Object> a) {
        a = new HashMap<String, Object>(a);
        if (args.get("search") == null) {
            ParseSedSyntax sed;
            sed = sedFactory.create(args.get("replace").toString());
            sed.parse();
            a.put("search", sed.getSearch());
            a.put("replace", sed.getReplace());
        } else {
            a.put("search", args.get("search"));
            a.put("replace", args.get("replace"));
        }
        return a;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(args).toString();
    }

}
