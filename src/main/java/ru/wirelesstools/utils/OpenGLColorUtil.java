package ru.wirelesstools.utils;

public class OpenGLColorUtil {

	public static float getGLRed(int color) {

		return (float) (color >> 16 & 255) / 255.0F;
	}

	public static float getGLBlue(int color) {

		return (float) (color >> 8 & 255) / 255.0F;
	}

	public static float getGLGreen(int color) {

		return (float) (color & 255) / 255.0F;
	}

	public static float getGLAlpha(int color) {

		return (float) (color >> 24 & 255) / 255.0F;
	}

	public static int toIntRGB(String color) {

		return Integer.parseInt(color.replace("0x", "").replace("#", ""), 16);
	}

	public static void printGLcolor(int color) {

		float red = (float) (color >> 16 & 255) / 255.0F;
		float blue = (float) (color >> 8 & 255) / 255.0F;
		float green = (float) (color & 255) / 255.0F;
		float alpha = (float) (color >> 24 & 255) / 255.0F;

		String stfred = String.format("%.2fF", red);
		String stfblue = String.format("%.2fF", blue);
		String stfgreen = String.format("%.2fF", green);
		String stfalpha = String.format("%.2fF", alpha);

		System.out.println("Int color: " + color);
		System.out.println("GL color: " + "(" + stfred + ", " + stfblue + ", " + stfgreen + ", " + stfalpha + ")");
	}

}
