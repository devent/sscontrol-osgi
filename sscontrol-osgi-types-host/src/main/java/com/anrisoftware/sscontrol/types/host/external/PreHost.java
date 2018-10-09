package com.anrisoftware.sscontrol.types.host.external;

import com.anrisoftware.sscontrol.types.app.external.AppException;

/**
 * Loaded before the host service is executed. That allows to setup the compiler
 * of the domain specific language (DSL) before the DSL is loaded and executed.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface PreHost {

    /**
     * Configures the specified compiler.
     *
     * @param compiler
     *            the compiler.
     *
     * @throws AppException
     *             if some error occur.
     */
    void configureCompiler(Object compiler) throws AppException;

    void configureServiceScript(HostServiceScript script) throws AppException;

}
