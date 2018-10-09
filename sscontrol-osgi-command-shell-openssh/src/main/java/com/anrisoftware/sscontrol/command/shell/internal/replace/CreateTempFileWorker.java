package com.anrisoftware.sscontrol.command.shell.internal.replace;

import static com.anrisoftware.sscontrol.command.replace.external.Replace.TMP_ARG;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.command.replace.external.CreateTmpException;
import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.google.inject.assistedinject.Assisted;

/**
 * Creates a temporary file to work with.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class CreateTempFileWorker implements Callable<File> {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface CreateTempFileWorkerFactory {

        CreateTempFileWorker create(@Assisted Map<String, Object> args);

    }

    private final Map<String, Object> args;

    @Inject
    CreateTempFileWorker(@Assisted Map<String, Object> args) {
        this.args = new HashMap<String, Object>(args);
    }

    @Override
    public File call() throws AppException {
        return createTmp();
    }

    private File createTmp() throws CreateTmpException {
        File tmp = (File) args.get(TMP_ARG);
        if (tmp != null) {
            return tmp;
        }
        try {
            return File.createTempFile("replace", ".dest");
        } catch (IOException e) {
            throw new CreateTmpException(e);
        }
    }

}
