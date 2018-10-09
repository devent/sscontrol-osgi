package com.anrisoftware.sscontrol.hosts.service.internal;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.anrisoftware.sscontrol.types.host.external.PreHost;
import com.anrisoftware.sscontrol.types.host.external.PreHostService;

/**
 * Hostname service pre-script.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class HostsPreScriptImpl implements PreHost {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface HostsPreScriptImplFactory extends PreHostService {
    }

    @Override
    public void configureCompiler(Object compiler) throws AppException {
        CompilerConfiguration cc = (CompilerConfiguration) compiler;
        ImportCustomizer imports = new ImportCustomizer();
        cc.addCompilationCustomizers(imports);
    }

    @Override
    public void configureServiceScript(HostServiceScript script)
            throws AppException {
    }
}
