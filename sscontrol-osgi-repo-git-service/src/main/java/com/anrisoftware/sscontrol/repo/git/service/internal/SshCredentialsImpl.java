package com.anrisoftware.sscontrol.repo.git.service.internal;

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.core.resources.ToURI;
import com.anrisoftware.sscontrol.repo.git.service.external.SshCredentials;
import com.google.inject.assistedinject.Assisted;

/**
 * SSH credentials.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class SshCredentialsImpl extends AbstractCredentials
        implements SshCredentials {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface SshCredentialsImplFactory extends CredentialsFactory {

    }

    private URI key;

    private final SshCredentialsImplLogger log;

    @Inject
    SshCredentialsImpl(SshCredentialsImplLogger log,
            @Assisted Map<String, Object> args) {
        super(args);
        this.log = log;
        parseArgs(args);
    }

    @Override
    public String getType() {
        return "ssh";
    }

    public void setKey(URI key) {
        this.key = key;
        log.keySet(this, key);
    }

    @Override
    public URI getKey() {
        return key;
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("key");
        setKey(ToURI.toURI(v).convert());
    }

}
