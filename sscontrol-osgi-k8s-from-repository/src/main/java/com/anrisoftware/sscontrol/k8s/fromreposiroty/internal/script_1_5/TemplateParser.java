package com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.script_1_5;

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

    String getTemplateName();

    /**
     * Returns the target file name.
     */
    String getFilename(String fileName);

    boolean getNeedCopyRepo();
}
