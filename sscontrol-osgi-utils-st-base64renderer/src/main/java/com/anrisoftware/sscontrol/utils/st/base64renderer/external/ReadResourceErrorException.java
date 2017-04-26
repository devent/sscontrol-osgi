package com.anrisoftware.sscontrol.utils.st.base64renderer.external;

import java.io.IOException;

import com.anrisoftware.sscontrol.types.external.app.AppException;

/**
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@SuppressWarnings("serial")
public class ReadResourceErrorException extends AppException {

    public ReadResourceErrorException(IOException cause, Object resource,
            String formatString) {
        super("Error read resource", cause);
        addContextValue("resource", resource.toString());
        addContextValue("format", formatString);
    }

}
