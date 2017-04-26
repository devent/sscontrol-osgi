package com.anrisoftware.sscontrol.repo.git.internal;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.repo.git.external.GitRepo;
import com.anrisoftware.sscontrol.repo.git.external.GitRepoHost;
import com.anrisoftware.sscontrol.types.external.ssh.SshHost;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Git</i> code repository host.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class GitRepoHostImpl implements GitRepoHost {

    private final GitRepo repo;

    private String host;

    private Integer port;

    /**
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface GitRepoHostImplFactory {

        GitRepoHost create(GitRepo repo, SshHost target);
    }

    @Inject
    GitRepoHostImpl(@Assisted GitRepo repo, @Assisted SshHost target) {
        this.repo = repo;
        this.host = target.getHost();
        this.port = target.getPort();
    }

    @Override
    public GitRepo getRepo() {
        return repo;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getHost() {
        return host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    @Override
    public String getHostAddress() throws UnknownHostException {
        return InetAddress.getByName(host).getHostAddress();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
