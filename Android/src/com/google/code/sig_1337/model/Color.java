package com.google.code.sig_1337.model;

public class Color {

	public static final Color BLUE = new Color(190, 208, 222);
	public static final Color GREEN = new Color(205, 213, 132);
	public static final Color GRAY = new Color(188, 182, 174);

	public static final Color LIGHT_BLUE = new Color(117, 213, 253);
	public static final Color DARK_BLUE = new Color(105, 126, 143);

	public static final Color LIGHT_WHITE = new Color(253, 250, 240);
	public static final Color DARK_WHITE = new Color(188, 182, 174);

	public static final Color LIGHT_ORANGE = new Color(253, 212, 117);
	public static final Color DARK_ORANGE = new Color(143, 125, 105);

	public static final Color BACKGROUND = new Color(248, 244, 232);

	public float red;
	public float green;
	public float blue;

	public Color(float red, float green, float blue) {
		super();
		this.red = red / 255f;
		this.green = green / 255f;
		this.blue = blue / 255f;
	}

}
