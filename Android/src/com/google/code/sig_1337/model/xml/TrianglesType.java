package com.google.code.sig_1337.model.xml;

/**
 * Enum for triangles types.
 */
public enum TrianglesType {

	/**
	 * Trou.
	 */
	Trou("trou");

	/**
	 * Parse the given string to the corresponding triangles type.
	 * 
	 * @param s
	 *            the string to parse.
	 * @return the corresponding triangles type.
	 */
	public static TrianglesType parse(String s) {
		for (TrianglesType t : values()) {
			if (s.equals(t.name)) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Name of this type.
	 */
	private final String name;

	/**
	 * Initializing constructor.
	 * 
	 * @param name
	 *            name of this type.
	 */
	private TrianglesType(String name) {
		this.name = name;
	}

	/**
	 * Get the name of this type.
	 * 
	 * @return the name of this type.
	 */
	public String getName() {
		return name;
	}

}
