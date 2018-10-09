package com.anrisoftware.sscontrol.fail2ban.service.external;

import java.util.List;

import com.anrisoftware.sscontrol.types.misc.external.DebugLogging;
import com.anrisoftware.sscontrol.types.host.external.HostService;

/**
 * <i>Fail2ban</i> service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Fail2ban extends HostService {

    /**
     * Returns the debug logging for the specified key.
     * <p>
     *
     * <pre>
     * service "fail2ban" with {
     *     debug "service", level: 1
     * }
     * </pre>
     */
    DebugLogging getDebugLogging();

    /**
     * Returns the jails.
     * <p>
     *
     * <pre>
     * service "fail2ban", notify: "admin@${target.host.address}" with { }
     * // or
     * service "fail2ban" with {
     *     notify address: "admin@${target.host.address}"
     *     ignore address: "192.0.0.1" // or
     *     ignore << "192.0.0.1, 192.0.0.2"
     *     banning retries: 3, time: "PT10M", backend: Backend.polling, type: Type.deny
     * }
     * </pre>
     */
    Jail getDefaultJail();

    /**
     * Returns the jails.
     * <p>
     *
     * <pre>
     * service "fail2ban" with {
     *     jail "apache", notify: "root@localhost" with {
     *     }
     * }
     * </pre>
     */
    List<Jail> getJails();

}
