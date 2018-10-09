package com.anrisoftware.sscontrol.app.main.internal.main;

import org.osgi.framework.BundleException;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class Main implements Runnable {

    private static HostApplication app;

    private static Thread mainThread;

    public static void main(String[] args) {
        app = new HostApplication(args);
        mainThread = new Thread(new Main(), "app");
        mainThread.start();
        try {
            Thread.sleep(5000);
            app.stop();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BundleException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            app.start();
        } catch (BundleException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                app.stop();
            } catch (BundleException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
