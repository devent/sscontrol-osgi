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
     * @author Erwin Müller <erwin.mueller@deventm.de>
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
