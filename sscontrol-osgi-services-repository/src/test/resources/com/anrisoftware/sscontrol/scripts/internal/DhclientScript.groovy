package com.anrisoftware.sscontrol.scripts.internal

dhclient.with {
    option 'rfc3442-classless-static-routes code 121 = array of unsigned integer 8'
    send 'host-name', 'andare.fugue.com';
    send 'host-name = gethostname()'
    request '!domain-name-servers'
    prepend 'domain-name-servers', '127.0.0.1'
    declare 'interface', 'eth0' with {
        // interface eth0
    }
    declare 'alias' with {
        // alias
    }
    declare 'lease' with {
        // lease
    }
}
