package com.anrisoftware.sscontrol.k8s.fromreposiroty.external;

import java.io.File;

import com.anrisoftware.sscontrol.types.app.external.AppException;

/**
 * Thrown if the specified template could not be parsed.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@SuppressWarnings("serial")
public class TemplateParseException extends AppException {

    public TemplateParseException(Throwable cause, File parentDirectory,
            String fileName) {
        super("Error parsing template", cause);
        addContextValue("parent-directory", parentDirectory);
        addContextValue("file-name", fileName);
    }
}
