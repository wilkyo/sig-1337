package com.google.code.sig_1337.model.xml.route;

import com.google.code.sig_1337.model.Color;

/**
 * Enums for route types.
 */
public enum RouteType {

	/**
	 * Itineraire.
	 */
	Itineraire("itineraire") {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Color getFill() {
			return Color.LIGHT_BLUE;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Color getStroke() {
			return Color.DARK_BLUE;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public float getFillSize() {
			return Route.getFillSize();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public float getStrokeSize() {
			return Route.getStrokeSize();
		}

	},
	/**
	 * Path.
	 */
	Path("chemin") {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Color getFill() {
			return Color.LIGHT_WHITE;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Color getStroke() {
			return Color.DARK_WHITE;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public float getFillSize() {
			return 0.00004f;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public float getStrokeSize() {
			return 0.00006f;
		}

	},
	/**
	 * Route.
	 */
	Route("route") {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Color getFill() {
			return Color.LIGHT_ORANGE;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Color getStroke() {
			return Color.DARK_ORANGE;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public float getFillSize() {
			return 0.00006f;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public float getStrokeSize() {
			return 0.00008f;
		}

	};

	/**
	 * Parse the given string to the corresponding route type.
	 * 
	 * @param s
	 *            the string to parse.
	 * @return the corresponding route type.
	 */
	public static RouteType parse(String s) {
		for (RouteType t : values()) {
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
	private RouteType(String name) {
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
	 * Get the fill color.
	 * 
	 * @return fill color.
	 */
	public abstract Color getFill();

	/**
	 * Get the stroke color.
	 * 
	 * @return stroke color.
	 */
	public abstract Color getStroke();

	/**
	 * Get the width.
	 * 
	 * @return width.
	 */
	public abstract float getFillSize();

	/**
	 * Get the width.
	 * 
	 * @return width.
	 */
	public abstract float getStrokeSize();

}
