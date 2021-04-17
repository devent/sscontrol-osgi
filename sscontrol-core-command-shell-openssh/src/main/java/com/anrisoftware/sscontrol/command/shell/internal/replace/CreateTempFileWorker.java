/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class CreateTempFileWorker implements Callable<File> {

    /**
     * 
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
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
