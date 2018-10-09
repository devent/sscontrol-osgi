package com.anrisoftware.sscontrol.command.shell.internal.copy;

import static com.anrisoftware.sscontrol.command.copy.external.Copy.REMOTE_TMP_ARGS;

import java.net.URL;
import java.util.Map;

import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Provides the command properties for a Linux system.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
@Singleton
public class LinuxPropertiesProvider extends AbstractContextPropertiesProvider {

    private static final URL res = LinuxPropertiesProvider.class
            .getResource("/copy_commands_linux.properties");

    LinuxPropertiesProvider() {
        super(LinuxPropertiesProvider.class, res);
    }

    public String getRemoteTempDir(Map<String, Object> args) {
        Object v = args.get(REMOTE_TMP_ARGS);
        if (v != null) {
            return v.toString();
        } else {
            return get().getProperty("remote_temp_directory");
        }
    }

}
