package com.anrisoftware.sscontrol.shell.internal.st;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.stringtemplate.v4.ST;

import com.google.inject.assistedinject.Assisted;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class StTemplate {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface StTemplateFactory {

        StTemplate create(@Assisted Map<String, Object> args,
                @Assisted Object parent);
    }

    private final Map<String, Object> args;

    private final String template;

    @Inject
    StTemplate(@Assisted Map<String, Object> args, @Assisted Object parent) {
        this.args = getArgs(args, parent);
        this.template = args.get("st").toString();
    }

    private Map<String, Object> getArgs(Map<String, Object> args,
            Object parent) {
        Map<String, Object> a = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, Object> v = (Map<String, Object>) args.get("vars");
        a.put("vars", v);
        a.put("parent", parent);
        return a;
    }

    /**
     * Returns the text from the template.
     */
    public String getText() {
        ST st = new ST(template);
        st.add("vars", args.get("vars"));
        st.add("parent", args.get("parent"));
        return st.render();
    }

}
