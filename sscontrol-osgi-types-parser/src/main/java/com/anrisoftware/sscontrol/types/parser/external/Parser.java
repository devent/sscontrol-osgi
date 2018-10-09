package com.anrisoftware.sscontrol.types.parser.external;

import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.host.external.HostServices;

/**
 * Script parser.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Parser {

    HostServices parse() throws AppException;
}
