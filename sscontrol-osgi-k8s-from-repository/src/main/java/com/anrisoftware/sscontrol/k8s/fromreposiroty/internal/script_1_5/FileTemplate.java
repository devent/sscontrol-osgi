package com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.script_1_5;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Parses the file via a template engine. The template engine is determined from
 * the file name.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface FileTemplate {

    String parseFile(File file, Map<String, Object> args, Charset encoding);

    /**
     * Returns the target file name.
     */
    String getFilename(File file);
}
