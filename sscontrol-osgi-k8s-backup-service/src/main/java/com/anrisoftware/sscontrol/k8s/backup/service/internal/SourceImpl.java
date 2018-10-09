package com.anrisoftware.sscontrol.k8s.backup.service.internal;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.k8s.backup.client.external.AbstractSource;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Source;
import com.google.inject.assistedinject.Assisted;

/**
 * Backup source.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class SourceImpl extends AbstractSource {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface SourceImplFactory {

        Source create(Map<String, Object> args);

    }

    @Inject
    SourceImpl(@Assisted Map<String, Object> args) {
        super(args);
    }


}
