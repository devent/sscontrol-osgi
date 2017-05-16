package com.anrisoftware.sscontrol.icinga.service.internal;

import java.util.Map;

import com.anrisoftware.sscontrol.icinga.service.external.Plugin;

/**
 * Plugin factory;
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface PluginFactory {

    Plugin create();

    Plugin create(Map<String, Object> args);
}
