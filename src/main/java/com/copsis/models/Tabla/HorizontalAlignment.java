package com.copsis.models.Tabla;

public enum HorizontalAlignment {
	LEFT, CENTER, RIGHT,JUSTIFY;

	public static HorizontalAlignment get(final String key) {
		switch (key == null ? "left" : key.toLowerCase().trim()) {
		case "left":
			return LEFT;
		case "center":
			return CENTER;
		case "right":
			return RIGHT;
		case "justify":
			return JUSTIFY;
			
		default:
			return LEFT;
		}
	}

}
