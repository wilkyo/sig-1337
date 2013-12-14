package com.google.code.sig_1337.model.xml.structure;

import com.google.code.sig_1337.model.Color;

public enum StructureType {

	Bassin {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Color getFill() {
			return Color.BLUE;
		}

	},
	Foret {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Color getFill() {
			return Color.GREEN;
		}

	},
	Building {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Color getFill() {
			return Color.GRAY;
		}

	};

	/**
	 * Get the fill color.
	 * 
	 * @return fill color.
	 */
	public abstract Color getFill();

	/**
	 * Get the hole color.
	 * 
	 * @return hole color.
	 */
	public Color getHole() {
		return Color.BACKGROUND;
	}

}
