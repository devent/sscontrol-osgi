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
package com.anrisoftware.sscontrol.fail2ban.service.internal;

import static java.lang.String.format;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import com.anrisoftware.sscontrol.fail2ban.service.external.Backend;
import com.anrisoftware.sscontrol.fail2ban.service.external.Type;
import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.anrisoftware.sscontrol.types.host.external.PreHost;
import com.anrisoftware.sscontrol.types.host.external.PreHostService;

/**
 * <i>Fail2ban</i> service pre-script.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class Fail2banPreScriptImpl implements PreHost {

    /**
     * 
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface HostnamePreScriptImplFactory extends PreHostService {
    }

    @Override
    public void configureCompiler(Object compiler) throws AppException {
        CompilerConfiguration cc = (CompilerConfiguration) compiler;
        ImportCustomizer imports = new ImportCustomizer();
        imports.addImports(
                format("com.anrisoftware.sscontrol.fail2ban.external.%s",
                        Backend.class.getSimpleName()));
        imports.addImports(
                format("com.anrisoftware.sscontrol.fail2ban.external.%s",
                        Type.class.getSimpleName()));
        cc.addCompilationCustomizers(imports);
    }

    @Override
    public void configureServiceScript(HostServiceScript script)
            throws AppException {
    }
}
