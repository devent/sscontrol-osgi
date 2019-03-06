package com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_13;

import com.anrisoftware.sscontrol.types.app.external.AppException;

/**
 * Thrown if there were errors in the manifest.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@SuppressWarnings("serial")
public class ManifestErrorsException extends AppException {

    public ManifestErrorsException(String name, String output, String error, String manifest) {
        super("Error in manifest");
        addContextValue("name", name);
        addContextValue("output", output);
        addContextValue("error", error);
        addContextValue("manifest", manifest);
    }
}
