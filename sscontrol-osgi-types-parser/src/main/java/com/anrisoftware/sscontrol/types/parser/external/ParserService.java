package com.anrisoftware.sscontrol.types.parser.external;

import java.net.URI;
import java.util.Map;

import com.anrisoftware.sscontrol.types.host.external.HostServices;

/**
 * Script parser service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface ParserService {

    Parser create(URI[] roots, String name, HostServices hostServices);

    Parser create(URI[] roots, String name, Map<String, Object> variables,
            HostServices hostServices);

}
