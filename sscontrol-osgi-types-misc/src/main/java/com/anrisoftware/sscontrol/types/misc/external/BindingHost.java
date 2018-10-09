package com.anrisoftware.sscontrol.types.misc.external;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Binding host and port.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface BindingHost {

    /**
     * Host bean.
     *
     * @author Erwin Müller, erwin.mueller@deventm.de
     * @since 1.0
     */
    public static final class Host {

        public final String host;

        public final Integer port;

        public Host(String host, Integer port) {
            this.host = host;
            this.port = port;
        }

        @Override
        public String toString() {
            ToStringBuilder b = new ToStringBuilder(this).append(host);
            if (port != null) {
                b.append(port);
            }
            return b.toString();
        }
    }

    /**
     * Returns the binding addresses to add.
     */
    List<Host> getAddedHosts();

    /**
     * Returns the binding addresses to removed.
     */
    List<Host> getRemovedHosts();
}
