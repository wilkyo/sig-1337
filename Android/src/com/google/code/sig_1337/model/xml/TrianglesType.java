package com.google.code.sig_1337.model.xml;

import java.nio.FloatBuffer;

import com.google.code.sig_1337.model.Sig1337Base;
import com.google.code.sig_1337.model.xml.structure.StructureType;

/**
 * Enum for triangles types.
 */
public enum TrianglesType {

	/**
	 * Filled.
	 */
	Filled("") {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fill(FloatBuffer colorBuffer, StructureType type) {
			type.fill(colorBuffer);
		}

	},

	/**
	 * Hole.
	 */
	Hole("trou") {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fill(FloatBuffer colorBuffer, StructureType type) {
			colorBuffer.put(Sig1337Base.BACKGROUND_COLOR);
			Sig1337Base.BACKGROUND_COLOR.position(0);
		}

	};

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

	/**
	 * Fill the given buffer.
	 * 
	 * @param colorBuffer
	 *            color buffer.
	 * @param type
	 *            type of the structure.
	 */
	public abstract void fill(FloatBuffer colorBuffer, StructureType type);

}
