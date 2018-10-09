package com.anrisoftware.sscontrol.k8s.restore.service.internal;

import java.text.ParseException;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.core.durationformat.DurationFormatFactory;
import com.anrisoftware.globalpom.core.durationsimpleformat.DurationSimpleFormatFactory;
import com.anrisoftware.sscontrol.k8s.backup.client.external.AbstractClient;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Client;
import com.google.inject.assistedinject.Assisted;

/**
 * Restore client.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ClientImpl extends AbstractClient {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ClientImplFactory {

        Client create(Map<String, Object> args);

    }

    @Inject
    ClientImpl(@Assisted Map<String, Object> args,
            DurationSimpleFormatFactory durationSimpleFormatFactory,
            DurationFormatFactory durationFormatFactory) throws ParseException {
        super(args, durationSimpleFormatFactory, durationFormatFactory);
    }

}
