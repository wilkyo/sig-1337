package com.google.code.sig_1337.model;

public class Vector {

	public double x;
	public double y;

	public Vector(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public void normaleGauche() {
		double x = this.x;
		double y = this.y;
		this.x = y;
		this.y = -x;
	}

	public void normaleDroite() {
		double x = this.x;
		double y = this.y;
		this.x = -y;
		this.y = x;
	}

	public void invert() {
		x = -x;
		y = -y;
	}

	public double longueur() {
		return Math.sqrt((x * x) + (y * y));
	}

	public void normaliser() {
		double longueur = longueur();
		if (longueur == 0) {
			x = Double.POSITIVE_INFINITY;
			y = Double.POSITIVE_INFINITY;
		} else {
			x /= longueur;
			y /= longueur;
		}
	}

	public void scale(double factor) {
		x *= factor;
		y *= factor;
	}

}
