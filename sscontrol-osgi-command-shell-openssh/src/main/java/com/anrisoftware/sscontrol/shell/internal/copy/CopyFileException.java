package com.anrisoftware.sscontrol.shell.internal.copy;

import java.io.File;
import java.io.IOException;

import com.anrisoftware.sscontrol.types.external.AppException;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class CopyFileException extends AppException {

    public CopyFileException(IOException e, File src, File dest) {
        super("Copy file error", e);
        addContextValue("src", src);
        addContextValue("dest", dest);
    }

}
