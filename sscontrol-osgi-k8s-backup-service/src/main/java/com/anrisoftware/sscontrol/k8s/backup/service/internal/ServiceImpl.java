package com.anrisoftware.sscontrol.k8s.backup.service.internal;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.k8s.backup.client.external.AbstractService;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Service;
import com.google.inject.assistedinject.Assisted;

/**
 * Service for backup.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ServiceImpl extends AbstractService {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ServiceImplFactory {

        Service create(Map<String, Object> args);

    }

    @Inject
    ServiceImpl(@Assisted Map<String, Object> args) {
        super(args);
    }


}
