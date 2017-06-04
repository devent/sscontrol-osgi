package com.anrisoftware.sscontrol.k8s.glusterfsheketi.internal.script_1_6;

import java.io.File;

import com.anrisoftware.sscontrol.types.app.external.AppException;

@SuppressWarnings("serial")
public class ApplyManifestException extends AppException {

    public ApplyManifestException(Throwable cause, File dir, String name,
            String content) {
        super("Error applying manifest", cause);
        addContextValue("file", new File(dir, name));
        addContextValue("content", content);
    }
}
