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
package com.anrisoftware.sscontrol.utils.debian.external

/**
 * Debian 10 test utilities.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
class Debian_10_TestUtils {

    static final URL catCommand = Debian_10_TestUtils.class.getResource('/com/anrisoftware/sscontrol/utils/debian/external/tests/debian_10_cat_cmd.txt')

    static final URL grepCommand = Debian_10_TestUtils.class.getResource('/com/anrisoftware/sscontrol/utils/debian/external/tests/debian_10_grep_cmd.txt')

    static final URL whichufwnotfoundCommand = Debian_10_TestUtils.class.getResource('/com/anrisoftware/sscontrol/utils/debian/external/tests/debian_10_which_ufw_not_found_cmd.txt')

    static final URL ufwActiveCommand = Debian_10_TestUtils.class.getResource('/com/anrisoftware/sscontrol/utils/debian/external/tests/debian_10_ufw_active_cmd.txt')
}
