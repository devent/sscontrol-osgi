package com.anrisoftware.sscontrol.utils.debian.external

/**
 * Debian 9 test utilities.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
class Debian_9_TestUtils {

    static final URL catCommand = Debian_9_TestUtils.class.getResource('/com/anrisoftware/sscontrol/utils/debian/external/tests/debian_9_cat_cmd.txt')

    static final URL grepCommand = Debian_9_TestUtils.class.getResource('/com/anrisoftware/sscontrol/utils/debian/external/tests/debian_9_grep_cmd.txt')

    static final URL whichufwnotfoundCommand = Debian_9_TestUtils.class.getResource('/com/anrisoftware/sscontrol/utils/debian/external/tests/debian_9_which_ufw_not_found_cmd.txt')

    static final URL ufwActiveCommand = Debian_9_TestUtils.class.getResource('/com/anrisoftware/sscontrol/utils/debian/external/tests/debian_9_ufw_active_cmd.txt')
}
