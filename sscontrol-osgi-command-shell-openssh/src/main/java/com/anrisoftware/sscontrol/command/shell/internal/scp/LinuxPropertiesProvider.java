package com.anrisoftware.sscontrol.command.shell.internal.scp;

import java.net.URL;
import java.util.Map;

import org.stringtemplate.v4.ST;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Provides the command properties for a Linux system.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class LinuxPropertiesProvider extends AbstractContextPropertiesProvider {

    private static final URL res = LinuxPropertiesProvider.class
            .getResource("/scp_commands_linux.properties");

    LinuxPropertiesProvider() {
        super(LinuxPropertiesProvider.class, res);
    }

    public String getRemoteTempDir() {
        return get().getProperty("remote_temp_directory");
    }

    public String getSetupCommands(Map<String, Object> args) {
        String str = get().getProperty("setup_commands");
        return new ST(str).add("args", args).render();
    }

    public String getCopyFileCommands(Map<String, Object> args) {
        String str = get().getProperty("copy_file_commands");
        return new ST(str).add("args", args).render();
    }

    public String getPushFileCommands(Map<String, Object> args) {
        String str = get().getProperty("push_file_commands");
        return new ST(str).add("args", args).render();
    }

    public String getCleanFileCommands(Map<String, Object> args) {
        String str = get().getProperty("clean_file_commands");
        return new ST(str).add("args", args).render();
    }

    public String getCheckFileCommands(Map<String, Object> args) {
        String str = get().getProperty("check_file_commands");
        return new ST(str).add("args", args).render();
    }

}
