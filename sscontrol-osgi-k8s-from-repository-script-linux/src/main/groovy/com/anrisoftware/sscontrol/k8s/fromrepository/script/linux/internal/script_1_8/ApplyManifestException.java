package com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_8;

import java.io.File;

import com.anrisoftware.sscontrol.types.app.external.AppException;

@SuppressWarnings("serial")
public class ApplyManifestException extends AppException {

    public ApplyManifestException(Throwable cause, File dir, String name) {
        super("Error applying manifest", cause);
        addContextValue("file", new File(dir, name));
    }
}
