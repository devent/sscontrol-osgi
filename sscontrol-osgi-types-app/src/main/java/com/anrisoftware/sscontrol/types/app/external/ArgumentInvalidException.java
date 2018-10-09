package com.anrisoftware.sscontrol.types.app.external;

import static java.lang.String.format;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ContextedRuntimeException;

@SuppressWarnings("serial")
public class ArgumentInvalidException extends ContextedRuntimeException {

    public static void checkNullArg(Object arg, String name)
            throws ArgumentInvalidException {
        if (arg == null) {
            throw new ArgumentInvalidException(name, "null");
        }
    }

    public static void checkBlankArg(String arg, String name)
            throws ArgumentInvalidException {
        if (StringUtils.isBlank(arg)) {
            throw new ArgumentInvalidException(name, "blank");
        }
    }

    public ArgumentInvalidException(String name, String descr) {
        super(format("Argument %s", descr));
        addContextValue("argument", name);
    }

}
