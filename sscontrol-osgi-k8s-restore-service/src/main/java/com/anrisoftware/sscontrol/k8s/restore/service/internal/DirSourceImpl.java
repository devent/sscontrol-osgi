package com.anrisoftware.sscontrol.k8s.restore.service.internal;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.k8s.backup.client.external.AbstractDirDestination;
import com.anrisoftware.sscontrol.k8s.backup.client.external.AbstractDirDestinationLogger;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Destination;
import com.google.inject.assistedinject.Assisted;

/**
 * Local directory backup source.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class DirSourceImpl extends AbstractDirDestination {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface DirSourceImplFactory {

        Destination create(Map<String, Object> args);

    }

    @Inject
    DirSourceImpl(@Assisted Map<String, Object> args,
            AbstractDirDestinationLogger log) {
        super(args, log);
    }

}
