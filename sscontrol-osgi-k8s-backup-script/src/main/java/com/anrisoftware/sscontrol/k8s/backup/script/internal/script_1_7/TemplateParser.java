package com.anrisoftware.sscontrol.k8s.backup.script.internal.script_1_7;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Parses the files via a template engine.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface TemplateParser {

    String parseFile(File parentDirectory, String fileName,
            Map<String, Object> args, Charset encoding);

    /**
     * Returns the template name.
     */
    String getTemplateName();

    /**
     * Checks if the file with the specified name is a Kubernetes manifest.
     */
    boolean isKubeFile(String fileName);

    /**
     * Returns the target file name.
     */
    String getFilename(String fileName);

}
