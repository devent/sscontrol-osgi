package com.anrisoftware.sscontrol.k8s.fromreposiroty.external;

import java.io.File;

import com.anrisoftware.sscontrol.types.app.external.AppException;

/**
 * Thrown if the specified template was not found.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@SuppressWarnings("serial")
public class TemplateNotFoundException extends AppException {

    public TemplateNotFoundException(File parentDirectory, String fileName,
            String name) {
        super("Template not found");
        addContextValue("parent-directory", parentDirectory);
        addContextValue("file-name", fileName);
        addContextValue("name", name);
    }
}
