package com.anrisoftware.sscontrol.fail2ban.service.external;

import java.util.List;

/**
 * <i>Fail2ban</i> jail.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Jail {

    /**
     * Returns the jail service name.
     * <p>
     *
     * <pre>
     * jail "apache" with {
     * }
     * </pre>
     */
    String getService();

    /**
     * Returns if the jail is enabled.
     * <p>
     *
     * <pre>
     * jail "apache", enabled: true with {
     * }
     * </pre>
     */
    Boolean getEnabled();

    /**
     * Returns the email address for notification.
     * <p>
     *
     * <pre>
     * jail "apache", notify: "root@localhost" with {
     * }
     * </pre>
     */
    String getNotify();

    /**
     * Returns the addresses to ignore.
     * <p>
     *
     * <pre>
     * jail "apache" with {
     *     ignore address: "192.0.0.1" // or
     *     ignore << "192.0.0.1, 192.0.0.2"
     * }
     * </pre>
     */
    List<String> getIgnoreAddresses();

    /**
     * Returns the jail banning arguments.
     * <p>
     *
     * <pre>
     * jail "apache" with {
     *     banning retries: 3, ...
     * }
     * </pre>
     */
    Banning getBanning();
}
