package com.anrisoftware.sscontrol.flanneldocker.script.upstream.external;

import com.anrisoftware.sscontrol.flanneldocker.service.external.FlannelDocker;
import com.anrisoftware.sscontrol.types.host.external.HostServices;

public interface NodesTargetsAddressListFactory {

    NodesTargetsAddressList create(FlannelDocker service, HostServices repo,
            Object parent);
}
