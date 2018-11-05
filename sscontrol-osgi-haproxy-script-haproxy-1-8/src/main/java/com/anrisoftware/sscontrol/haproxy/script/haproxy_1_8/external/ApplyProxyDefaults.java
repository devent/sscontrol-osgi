package com.anrisoftware.sscontrol.haproxy.script.haproxy_1_8.external;

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase;
import com.anrisoftware.sscontrol.haproxy.service.external.Proxy;

/**
* Apply defaults to proxy based on the proxy name.
*
* @author Erwin Müller <erwin.mueller@deventm.de>
* @version 1.0
*/
public interface ApplyProxyDefaults {

    String getName();
    
    void applyDefaults(ScriptBase parent, Proxy proxy);
}
