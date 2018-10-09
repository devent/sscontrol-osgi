package com.anrisoftware.sscontrol.types.misc.external;

/**
 * Named binding address.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum BindingAddress {

	/**
	 * Loopback address {@code 127.0.0.1}
	 */
	loopback("127.0.0.1"),

	/**
	 * Local host address {@code 127.0.0.1}
	 */
	local("127.0.0.1"),

	/**
	 * All address {@code 0.0.0.0}
	 */
	all("0.0.0.0");

    private String address;

	private BindingAddress(String address) {
        this.address = address;
	}

    public String getAddress() {
		return address;
	}

	@Override
	public String toString() {
        return address;
	}
}
