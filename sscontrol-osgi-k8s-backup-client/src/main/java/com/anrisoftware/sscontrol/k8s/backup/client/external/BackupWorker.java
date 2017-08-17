package com.anrisoftware.sscontrol.k8s.backup.client.external;

/**
 * Starts the backup.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface BackupWorker {

    void init();

    void before();

    void start(Object client);

    void after();

    void finally1();

    Object getRsyncDeploy();

    int getRsyncPort();
}
