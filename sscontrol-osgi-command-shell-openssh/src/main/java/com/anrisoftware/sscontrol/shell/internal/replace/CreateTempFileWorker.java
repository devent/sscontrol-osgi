/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-command-shell-openssh.
 *
 * sscontrol-osgi-command-shell-openssh is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-command-shell-openssh is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-command-shell-openssh. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.shell.internal.replace;

import static com.anrisoftware.sscontrol.replace.external.Replace.TMP_ARG;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.replace.external.CreateTmpException;
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
