package com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_9.etcd_3_2;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * <i>Etcd 3.2 Debian 9</i> properties provider from
 * {@code "/etcd_3_2_debian_9.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class EtcdDebianProperties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = EtcdDebianProperties.class.getResource("/etcd_3_2_debian_9.properties");

    EtcdDebianProperties() {
        super(EtcdDebianProperties.class, RESOURCE);
    }
}
