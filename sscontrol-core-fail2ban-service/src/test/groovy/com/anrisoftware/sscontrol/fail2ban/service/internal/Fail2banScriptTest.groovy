/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.fail2ban.service.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.joda.time.Duration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import com.anrisoftware.globalpom.core.durationformat.DurationFormatModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.fail2ban.service.external.Backend
import com.anrisoftware.sscontrol.fail2ban.service.external.Fail2ban
import com.anrisoftware.sscontrol.fail2ban.service.external.Jail
import com.anrisoftware.sscontrol.fail2ban.service.external.Type
import com.anrisoftware.sscontrol.fail2ban.service.internal.Fail2banImpl.Fail2banImplFactory
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesServiceModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule
import com.anrisoftware.sscontrol.services.internal.targets.TargetsServiceModule
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
import com.anrisoftware.sscontrol.types.ssh.external.Ssh
import com.anrisoftware.sscontrol.utils.systemmappings.internal.SystemNameMappingsModule
import com.google.inject.Guice

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class Fail2banScriptTest {

    @Inject
    HostServicesImplFactory servicesFactory

    @Inject
    Fail2banImplFactory hostnameFactory

    @Test
    void "fail2ban service"() {
        def testCases = [
            [
                input: """
service "fail2ban" with {
    jail "apache"
}
""",
                expected: { HostServices services ->
                    assert services.getServices('fail2ban').size() == 1
                    Fail2ban fail = services.getServices('fail2ban')[0] as Fail2ban
                    assert fail.defaultJail.service == "DEFAULT"
                    assert fail.jails.size() == 1
                    Jail j = fail.jails[0]
                    assert j.service == "apache"
                },
            ],
            [
                input: """
service "fail2ban", notify: "admin@localhost" with {
    jail "apache"
}
""",
                expected: { HostServices services ->
                    assert services.getServices('fail2ban').size() == 1
                    Fail2ban fail = services.getServices('fail2ban')[0] as Fail2ban
                    assert fail.defaultJail.service == "DEFAULT"
                    assert fail.defaultJail.notify == "admin@localhost"
                    assert fail.jails.size() == 1
                    Jail j = fail.jails[0]
                    assert j.service == "apache"
                },
            ],
            [
                input: """
service "fail2ban" with {
    notify "admin@localhost"
    jail "apache"
}
""",
                expected: { HostServices services ->
                    assert services.getServices('fail2ban').size() == 1
                    Fail2ban fail = services.getServices('fail2ban')[0] as Fail2ban
                    assert fail.defaultJail.service == "DEFAULT"
                    assert fail.defaultJail.notify == "admin@localhost"
                    assert fail.jails.size() == 1
                    Jail j = fail.jails[0]
                    assert j.service == "apache"
                },
            ],
            [
                input: """
service "fail2ban" with {
    notify address: "admin@localhost"
    jail "apache"
}
""",
                expected: { HostServices services ->
                    assert services.getServices('fail2ban').size() == 1
                    Fail2ban fail = services.getServices('fail2ban')[0] as Fail2ban
                    assert fail.defaultJail.service == "DEFAULT"
                    assert fail.defaultJail.notify == "admin@localhost"
                    assert fail.jails.size() == 1
                    Jail j = fail.jails[0]
                    assert j.service == "apache"
                },
            ],
            [
                input: """
service "fail2ban" with {
    jail "apache", notify: "admin@localhost"
}
""",
                expected: { HostServices services ->
                    assert services.getServices('fail2ban').size() == 1
                    Fail2ban fail = services.getServices('fail2ban')[0] as Fail2ban
                    assert fail.defaultJail.service == "DEFAULT"
                    assert fail.jails.size() == 1
                    Jail j = fail.jails[0]
                    assert j.service == "apache"
                    assert j.notify == "admin@localhost"
                },
            ],
            [
                input: """
service "fail2ban" with {
    jail "apache" with {
        notify "admin@localhost"
    }
}
""",
                expected: { HostServices services ->
                    assert services.getServices('fail2ban').size() == 1
                    Fail2ban fail = services.getServices('fail2ban')[0] as Fail2ban
                    assert fail.defaultJail.service == "DEFAULT"
                    assert fail.jails.size() == 1
                    Jail j = fail.jails[0]
                    assert j.service == "apache"
                    assert j.notify == "admin@localhost"
                },
            ],
            [
                input: """
service "fail2ban" with {
    jail "apache" with {
        notify address: "admin@localhost"
    }
}
""",
                expected: { HostServices services ->
                    assert services.getServices('fail2ban').size() == 1
                    Fail2ban fail = services.getServices('fail2ban')[0] as Fail2ban
                    assert fail.defaultJail.service == "DEFAULT"
                    assert fail.jails.size() == 1
                    Jail j = fail.jails[0]
                    assert j.service == "apache"
                    assert j.notify == "admin@localhost"
                },
            ],
            [
                input: """
import com.anrisoftware.sscontrol.fail2ban.service.external.Backend
import com.anrisoftware.sscontrol.fail2ban.service.external.Type

service "fail2ban" with {
    banning retries: 3, time: "PT10M", backend: Backend.polling, type: Type.deny
    jail "apache"
}
""",
                expected: { HostServices services ->
                    assert services.getServices('fail2ban').size() == 1
                    Fail2ban fail = services.getServices('fail2ban')[0] as Fail2ban
                    assert fail.defaultJail.service == "DEFAULT"
                    assert fail.defaultJail.notify == null
                    assert fail.defaultJail.banning.retries == 3
                    assert fail.defaultJail.banning.time == Duration.standardMinutes(10)
                    assert fail.defaultJail.banning.backend == Backend.polling
                    assert fail.defaultJail.banning.type == Type.deny
                    assert fail.jails.size() == 1
                    Jail j = fail.jails[0]
                    assert j.service == "apache"
                },
            ],
            [
                input: """
service "fail2ban" with {
    ignore << "128.0.0.1"
    jail "apache"
}
""",
                expected: { HostServices services ->
                    assert services.getServices('fail2ban').size() == 1
                    Fail2ban fail = services.getServices('fail2ban')[0] as Fail2ban
                    assert fail.defaultJail.service == "DEFAULT"
                    assert fail.defaultJail.notify == null
                    assert fail.defaultJail.ignoreAddresses.size() == 1
                    assert fail.defaultJail.ignoreAddresses.containsAll(['128.0.0.1'])
                    assert fail.defaultJail.banning.retries == null
                    assert fail.defaultJail.banning.time == null
                    assert fail.defaultJail.banning.backend == null
                    assert fail.defaultJail.banning.type == null
                    assert fail.jails.size() == 1
                    Jail j = fail.jails[0]
                    assert j.service == "apache"
                },
            ],
            [
                input: """
service "fail2ban" with {
    ignore address: "128.0.0.1"
    jail "apache"
}
""",
                expected: { HostServices services ->
                    assert services.getServices('fail2ban').size() == 1
                    Fail2ban fail = services.getServices('fail2ban')[0] as Fail2ban
                    assert fail.defaultJail.service == "DEFAULT"
                    assert fail.defaultJail.ignoreAddresses.size() == 1
                    assert fail.defaultJail.ignoreAddresses.containsAll(['128.0.0.1'])
                    assert fail.jails.size() == 1
                    Jail j = fail.jails[0]
                    assert j.service == "apache"
                },
            ],
            [
                input: """
service "fail2ban" with {
    jail "apache" with {
        ignore << "128.0.0.1"
    }
}
""",
                expected: { HostServices services ->
                    assert services.getServices('fail2ban').size() == 1
                    Fail2ban fail = services.getServices('fail2ban')[0] as Fail2ban
                    assert fail.defaultJail.service == "DEFAULT"
                    assert fail.jails.size() == 1
                    Jail j = fail.jails[0]
                    assert j.service == "apache"
                    assert j.ignoreAddresses.size() == 1
                    assert j.ignoreAddresses.containsAll(['128.0.0.1'])
                },
            ],
            [
                input: """
service "fail2ban" with {
    jail "apache" with {
        ignore address: "128.0.0.1"
    }
}
""",
                expected: { HostServices services ->
                    assert services.getServices('fail2ban').size() == 1
                    Fail2ban fail = services.getServices('fail2ban')[0] as Fail2ban
                    assert fail.defaultJail.service == "DEFAULT"
                    assert fail.jails.size() == 1
                    Jail j = fail.jails[0]
                    assert j.service == "apache"
                    assert j.ignoreAddresses.size() == 1
                    assert j.ignoreAddresses.containsAll(['128.0.0.1'])
                },
            ],
            [
                input: """
service "fail2ban" with {
    jail "apache" with {
        banning app: "Apache"
    }
}
""",
                expected: { HostServices services ->
                    assert services.getServices('fail2ban').size() == 1
                    Fail2ban fail = services.getServices('fail2ban')[0] as Fail2ban
                    assert fail.defaultJail.service == "DEFAULT"
                    assert fail.jails.size() == 1
                    Jail j = fail.jails[0]
                    assert j.service == "apache"
                    assert j.banning.app == "Apache"
                },
            ],
            [
                input: """
service "fail2ban" with {
    jail "apache", port: 22222
}
""",
                expected: { HostServices services ->
                    assert services.getServices('fail2ban').size() == 1
                    Fail2ban fail = services.getServices('fail2ban')[0] as Fail2ban
                    assert fail.defaultJail.service == "DEFAULT"
                    assert fail.jails.size() == 1
                    Jail j = fail.jails[0]
                    assert j.service == "apache"
                    assert j.port == 22222
                },
            ],
            [
                input: """
service "fail2ban" with {
    jail "apache" with {
        port number: 22222
    }
}
""",
                expected: { HostServices services ->
                    assert services.getServices('fail2ban').size() == 1
                    Fail2ban fail = services.getServices('fail2ban')[0] as Fail2ban
                    assert fail.defaultJail.service == "DEFAULT"
                    assert fail.jails.size() == 1
                    Jail j = fail.jails[0]
                    assert j.service == "apache"
                    assert j.port == 22222
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            log.info '\n#### {}. case: {}', k, test
            def services = servicesFactory.create()
            services.targets.addTarget([getGroup: {'default'}, getHosts: { []}] as Ssh)
            services.putAvailableService 'fail2ban', hostnameFactory
            Eval.me 'service', services, test.input as String
            Closure expected = test.expected
            expected services
        }
    }

    @BeforeEach
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new Fail2banModule(),
                new DebugLoggingModule(),
                new TypesModule(),
                new StringsModule(),
                new DurationFormatModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new TargetsServiceModule(),
                new PropertiesModule(),
                new PropertiesUtilsModule(),
                new SystemNameMappingsModule(),
                new HostServicePropertiesServiceModule(),
                ).injectMembers(this)
    }
}
