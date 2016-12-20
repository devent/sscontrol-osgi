package com.anrisoftware.sscontrol.fail2ban.external;

import org.joda.time.Duration;

/**
 * The jail banning arguments.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Banning {

    /**
     * Returns the maximum retries.
     * <p>
     *
     * <pre>
     * jail "apache" with {
     *     banning retries: 3
     * }
     * </pre>
     */
    Integer getRetries();

    /**
     * Returns the banning time.
     * <p>
     *
     * <pre>
     * jail "apache" with {
     *     banning time: "PT10M"
     * }
     * </pre>
     */
    Duration getTime();

    /**
     * Returns the banning backend.
     * <p>
     *
     * <pre>
     * jail "apache" with {
     *     banning backend: Backend.polling
     * }
     * </pre>
     */
    Backend getBackend();

    /**
     * Returns the banning type.
     * <p>
     *
     * <pre>
     * jail "apache" with {
     *     banning type: Type.deny
     * }
     * </pre>
     */
    Type getType();

    /**
     * Returns the banning application name.
     * <p>
     *
     * <pre>
     * jail "apache" with {
     *     banning app: "OpenSSH"
     * }
     * </pre>
     */
    String getApp();

    /**
     * Returns the banning action.
     * <p>
     *
     * <pre>
     * jail "apache" with {
     *     banning action: "%(banaction)s[name=%(__name__)s, port="%(port)s", protocol="%(protocol)s", chain="%(chain)s"]"
     * }
     * </pre>
     */
    String getAction();
}
