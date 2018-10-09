package com.anrisoftware.sscontrol.k8s.backup.service.internal;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.k8s.backup.client.external.AbstractDirDestination;
import com.anrisoftware.sscontrol.k8s.backup.client.external.AbstractDirDestinationLogger;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Destination;
import com.google.inject.assistedinject.Assisted;

/**
 * Local directory backup destination.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class DirDestinationImpl extends AbstractDirDestination {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface DirDestinationImplFactory {

        Destination create(Map<String, Object> args);

    }

    @Inject
    DirDestinationImpl(@Assisted Map<String, Object> args,
            AbstractDirDestinationLogger log) {
        super(args, log);
    }

}
