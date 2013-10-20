package com.google.code.sig_1337.model.xml;

/**
 * Building.
 */
public class Building implements IBuilding {

	/**
	 * Its name.
	 */
	private final String name;

	/**
	 * Initializing constructor.
	 * 
	 * @param name
	 *            its name.
	 */
	public Building(String name) {
		super();
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

}
